package info.teksol.decompiler;

import info.teksol.mindcode.compiler.generator.AstContext;
import info.teksol.mindcode.compiler.instructions.InstructionProcessor;
import info.teksol.mindcode.logic.InstructionParameterType;
import info.teksol.mindcode.logic.Opcode;

import java.util.*;
import java.util.stream.Collectors;

public class MlogParser {
    private final InstructionProcessor instructionProcessor;
    private final String mlog;

    private final List<Object> content = new ArrayList<>();
    private final Map<Integer, String> labels = new HashMap<>();
    private final Set<String> usedLabels = new HashSet<>();

    private static final AstContext STATIC_AST_CONTEXT = AstContext.createStaticRootNode();

    public MlogParser(InstructionProcessor instructionProcessor, String mlog) {
        this.instructionProcessor = instructionProcessor;
        this.mlog = mlog;
    }

    public ParsedMlog parse() {
        while (pos < mlog.length()) {
            switch (mlog.charAt(pos)) {
                case '\n', '\r', ';', '\t', ' ' -> pos++;
                default -> processStatement();
            }
        }

        return new ParsedMlog(labels, content);
    }

    private int pos;
    private int start;
    private int labelIndex;
    private final List<String> tokens = new ArrayList<>();

    private String error(String message) {
        skipLine();
        return mlog.substring(start, pos) + ": " + message;
    }

    private boolean endOfLine() {
        return pos >= mlog.length() || mlog.charAt(pos) == '\n' || mlog.charAt(pos) == '\r' || mlog.charAt(pos) == ';';
    }

    private void skipLine() {
        while (!endOfLine()) pos++;
    }

    private void processStatement() {
        boolean expectSeparator = false;
        tokens.clear();
        start = pos;

        while (pos < mlog.length()) {
            if (endOfLine()) break;

            char c = mlog.charAt(pos);

            if (expectSeparator && c != ' ' && c != '#' && c != '\t') {
                content.add(error("Missing separator after string or token."));
                return;
            }

            expectSeparator = false;

            if (c == '#') {
                skipLine();
                break;
            } else if (c == '"') {
                String string = parseString();
                if (string == null) {
                    content.add(error("Missing closing quote \" before end of line."));
                    return;
                }
                tokens.add(string);
                expectSeparator = true;
            } else if (c != ' ' && c != '\t') {
                tokens.add(parseToken());
                expectSeparator = true;
            } else {
                pos++;
            }
        }

        if (!tokens.isEmpty()) {
            if (tokens.size() == 1 && tokens.get(0).charAt(tokens.get(0).length() - 1) == ':') {
                // Symbolic label
                labels.put(content.size(), tokens.get(0));
            } else {
                Opcode opcode = Opcode.fromOpcode(tokens.get(0));
                if (opcode != null) {
                    List<MlogExpression> arguments = tokens.stream().skip(1).map(this::arg)
                            .collect(Collectors.toCollection(ArrayList::new));
                    List<InstructionParameterType> parameters = instructionProcessor.getParameters(opcode, arguments);
                    while (arguments.size() < parameters.size()) {
                        arguments.add(arg("0"));
                    }
                    content.add(new InstructionExpression(opcode, arguments.subList(0, parameters.size()),parameters));
                } else {
                    content.add(error("Unrecognized opcode '" + tokens.get(0) + "'."));
                }
            }
        }
    }

    private MlogVariable arg(String mlog) {
        return new MlogVariable(mlog);
    }

    private String parseString() {
        int from = pos;

        while (++pos < mlog.length()) {
            var c = mlog.charAt(pos);
            if (c == '\n' || c == '\r' || c == '"') {
                break;
            }
        }

        if (pos >= mlog.length() || mlog.charAt(pos) != '"') {
            return null;
        } else {
            return mlog.substring(from, ++pos);
        }
    }

    private String parseToken() {
        int from = pos;

        while (pos < mlog.length()) {
            char c = mlog.charAt(pos);
            if (c == '\n' || c == '\r' || c == ' ' || c == '#' || c == '\t' || c == ';') break;
            pos++;
        }

        return mlog.substring(from, pos);
    }
}
