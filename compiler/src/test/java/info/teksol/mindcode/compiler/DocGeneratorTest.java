package info.teksol.mindcode.compiler;

import info.teksol.mindcode.*;
import info.teksol.mindcode.ast.AstNode;
import info.teksol.mindcode.ast.FunctionDeclaration;
import info.teksol.mindcode.ast.FunctionParameter;
import info.teksol.mindcode.compiler.optimization.OptimizationLevel;
import info.teksol.mindcode.logic.ProcessorVersion;
import info.teksol.util.ExpectedMessages;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;

@Order(5)
public class DocGeneratorTest extends AbstractGeneratorTest {
    private static final String PREFIX = "#generate";
    private static final String SOURCE_FILE = "src/test/resources/library/doc/SYSTEM-LIBRARY_template.markdown";
    private static final String TARGET_FILE = "../doc/syntax/SYSTEM-LIBRARY.markdown";
    private static final String LIBRARY_DIRECTORY = "src/main/resources/library";
    private static final String FOOTPRINT = "@footprint";

    private PrintWriter writer;

    @Test
    void generateLibraryDocumentation() throws IOException {
        Path path = Path.of(SOURCE_FILE);

        try (final PrintWriter w = new PrintWriter(TARGET_FILE, StandardCharsets.UTF_8); Stream<String> lineStream = Files.lines(path)) {
            writer = w;
            lineStream.forEach(this::processLine);
            writer = null;
        }
    }

    private void processLine(String line)  {
        if (line.startsWith(PREFIX)) {
            processAllLibraries();
        } else {
            writer.println(line);
        }
    }

    private void processAllLibraries() {
        try (Stream<Path> stream = Files.list(Paths.get(LIBRARY_DIRECTORY))) {
            List<Path> files = stream
                    .filter(f -> f.toString().endsWith(".mnd"))
                    .filter(f -> !f.toString().contains("unittests.mnd"))
                    //.filter(f -> f.toString().endsWith("units.mnd"))
                    .toList();
            assertFalse(files.isEmpty(), "Expected to find at least one script in " + LIBRARY_DIRECTORY + "; found none");
            for (Path file : files) {
                processLibrary(file);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    String libraryName;
    List<FunctionDeclaration> functions = new ArrayList<>();
    FunctionDeclaration declaration;

    private void processLibrary(Path file) throws IOException {
        String fileName = file.getFileName().toString();
        libraryName = fileName.substring(0, fileName.lastIndexOf("."));
        String code = Files.readString(file);

        // Parse and process the module
        functions.clear();
        AstNode node = translateToAst(ExpectedMessages.create().addLevelsUpTo(MessageLevel.WARNING).ignored(),
                code, new ArrayList<>());
        processNode(node);

        writer.println();
        writer.println("## " + firstUpperCase(libraryName) +  " library");
        writer.println();
        processModuleDoc(code);

        functions.stream().filter(function -> !function.getName().startsWith("_")).forEach(this::processFunction);
    }

    private String firstUpperCase(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private void processModuleDoc(String code) {
        for (String line : code.lines().toList()) {
            if (!line.startsWith("//")) {
                return;
            } else if (line.startsWith("//*")) {
                writer.println(line.substring(line.startsWith("//* ") ? 4 : 3).trim());
            }
        }
    }

    private void processNode(AstNode node) {
        if (node instanceof FunctionDeclaration fd) {
            functions.add(fd);
        }
        node.getChildren().forEach(this::processNode);
    }

    private void processFunction(FunctionDeclaration declaration) {
        this.declaration = declaration;
        writer.println();
        writer.println("### " + declaration.getName());
        writer.println();
        writeFunction();

        writer.println();
        writer.printf("| %-30s | %10s | %10s |%n", "Optimization goal:", "Speed", "Size");
        writer.printf("|-%-30s |-%10s:|-%10s:|%n", dashes(30), dashes(10), dashes(10));
        
        if (!declaration.isVarArg()) {
            int speed = measureFootprint(null, GenerationGoal.SPEED);
            int size = measureFootprint(null, GenerationGoal.SIZE);
            if (!declaration.isNoinline()) {
                writer.printf("| %-30s | %10s | %10s |%n", "Inlined function", speed, size);
            } else {
                // TODO Implement if noinline function is ever added to the library
                throw new UnsupportedOperationException("Size calculation for noinline functions is not supported");
            }
            if (!declaration.isInline()) {
                int callSize = declaration.getCallSize();
                // +1 for the return from the function call
                writer.printf("| %-30s | %10s | %10s |%n", "Function body", speed + 1, size + 1);
                writer.printf("| %-30s | %10s | %10s |%n", "Function call", callSize, callSize);
            }
        } else {
            List<FootprintConfig> footprintConfigs = processFootprints(declaration.getCodeDoc());
            footprintConfigs.forEach(footprintConfig -> {
                int speed = measureFootprint(footprintConfig, GenerationGoal.SPEED);
                int size = measureFootprint(footprintConfig, GenerationGoal.SIZE);
                writer.printf("| %-30s | %10s | %10s |%n", footprintConfig.title, speed, size);
            });
        }

        if (declaration.getCodeDoc() != null) {
            writer.println();
            writeCodeDoc(declaration.getCodeDoc());
        }
    }

    private final List<FootprintConfig> defaultFootprintConfigs = List.of(
            new FootprintConfig("Five arguments in total", null, 5),
            new FootprintConfig("Ten arguments in total", null, 10),
            new FootprintConfig("Twenty arguments in total", null, 20)
    );

    private List<FootprintConfig> processFootprints(String codeDoc) {
        if (codeDoc == null) {
            return defaultFootprintConfigs;
        } else {
            List<FootprintConfig> list = codeDoc.lines().filter(l -> l.contains(FOOTPRINT)).map(this::processFootprint).toList();
            return list.isEmpty() ? defaultFootprintConfigs : list;
        }
    }

    private FootprintConfig processFootprint(String directive) {
        int index = directive.indexOf(FOOTPRINT);
        String trimmed = directive.substring(index + FOOTPRINT.length()).trim();
        int index2 = trimmed.indexOf(':');
        if (index2 == -1) {
            throw new MindcodeInternalError("Malformed %s directive", FOOTPRINT);
        }
        String title = trimmed.substring(0, index2);
        String declaration = trimmed.substring(index2 + 1).trim();
        if (declaration.startsWith("*")) {
            return new FootprintConfig(title, null, Integer.parseInt(declaration.substring(1)));
        } else {
            return new FootprintConfig(title, declaration, 0);
        }
    }

    private int measureFootprint(FootprintConfig footprintConfig, GenerationGoal goal) {
        int createdParams = 0;

        StringBuilder code = new StringBuilder()
                .append("require ").append(libraryName).append(";\n")
                .append("\n");

        List<FunctionParameter> inputs = declaration.getParams().stream()
                .filter(p -> !p.isVarArgs() && p.isInput())
                .toList();

        createdParams += inputs.size();
        inputs.forEach(parameter -> code.append("param prm_").append(parameter.getName()).append(" = 0;\n"));

        List<String> additionalOutputs = new ArrayList<>();
        declaration.getParams().stream()
                .filter(p -> !p.isVarArgs() && p.isOutput())
                .map(FunctionParameter::getName)
                .forEach(additionalOutputs::add);

        String varargs = null;
        if (footprintConfig != null) {
            if (footprintConfig.declarations == null) {
                int count = footprintConfig.argCount - declaration.getParams().size() + 1;
                if (count < 0) {
                    throw new MindcodeInternalError("Invalid argument count");
                }
                for (int i = 0; i < count; i++) {
                    code.append("param prm_").append(i).append(" = ").append(i).append(";\n");
                }
                createdParams += count;
                varargs = IntStream.range(0, count).mapToObj(i -> "prm_" + i).collect(Collectors.joining(", "));
            } else {
                varargs = footprintConfig.declarations;
                for (String vararg : footprintConfig.declarations.split(" *, *")) {
                    int index = vararg.indexOf("prm_");
                    if (index != -1) {
                        String name = vararg.substring(index);
                        code.append("param ").append(name).append(" = ").append(name.substring(4)).append(";\n");
                        createdParams++;
                    }
                    if (vararg.startsWith("out ")) {
                        additionalOutputs.add(vararg.substring(4));
                    }
                }
            }
        }

        code.append("\n");
        if (!declaration.isProcedure()) code.append("print(");
        code.append(declaration.getName()).append("(");

        boolean first = true;
        for (FunctionParameter parameter : declaration.getParams()) {
            if (first) {
                first = false;
            } else {
                code.append(", ");
            }
            if (parameter.isVarArgs()) {
                if (varargs != null) {
                    code.append(varargs);
                }
            } else {
                if (parameter.isOutput()) code.append("out ");
                code.append("prm_").append(parameter.getName());
            }
        }
        code.append(")");
        if (!declaration.isProcedure()) code.append(")");
        code.append(";\n");

        if (!additionalOutputs.isEmpty()) {
            code.append(additionalOutputs.stream().collect(Collectors.joining(",", "print(", ");\n")));
        }

        //System.out.println(code);

        CompilerProfile profile = createCompilerProfile()
                .setProcessorVersion(ProcessorVersion.MAX)
                .setAllOptimizationLevels(OptimizationLevel.ADVANCED)
                .setGoal(goal)
                .setSignature(false);

        CompilerOutput<String> result = new MindcodeCompiler(profile).compile(InputFile.createSourceFiles(code.toString()));
        if (result.hasErrors() || result.hasWarnings()) {
            System.out.println(code);
            result.formatMessages(MindcodeMessage::isErrorOrWarning,
                            m -> m.formatMessage(InputPosition::formatForIde))
                    .forEach(System.out::println);
            throw new MindcodeInternalError("Errors or warnings while measuring footprint.");
        }

        return (int) result.output().lines().count()
                - (declaration.isProcedure() ? 0 : 1)
                - createdParams
                - additionalOutputs.size();
    }

    private void writeFunction() {
        writer.print("**Definition:** `");
        if (declaration.isInline()) writer.print("inline ");
        if (declaration.isNoinline()) writer.print("noinline ");
        writer.print(declaration.isProcedure() ? "void " : "def ");
        writer.print(declaration.getName());
        writer.print("(");
        boolean first = true;
        for (FunctionParameter parameter : declaration.getParams()) {
            if (first) {
                first = false;
            } else {
                writer.print(", ");
            }
            if (parameter.hasInModifier()) writer.print("in ");
            if (parameter.hasOutModifier()) writer.print("out ");
            writer.print(parameter.getName().substring(parameter.getName().charAt(0) == '_' ? 1 : 0));
            if (parameter.isVarArgs()) writer.print("...");
        }
        writer.println(")`");
    }

    private void writeCodeDoc(String codeDoc) {
        if (!codeDoc.contains("\n")) {
            writer.println(codeDoc.trim());
        } else {
            codeDoc.lines().forEach(this::writeLine);
        }
    }

    private void writeLine(String line) {
        String trimmed = line.trim();
        if (!trimmed.contains(FOOTPRINT)) {
            if (trimmed.startsWith("*")) {
                writer.println(trimmed.substring(trimmed.startsWith("* ") ? 2 : 1));
            } else if (!trimmed.isEmpty()) {
                writer.println(line);
            }
        }
    }

    private record FootprintConfig(String title, String declarations, int argCount) {
    }

    private String dashes(int count) {
        final char[] array = new char[count];
        Arrays.fill(array, '-');
        return new String(array);
    }
}
