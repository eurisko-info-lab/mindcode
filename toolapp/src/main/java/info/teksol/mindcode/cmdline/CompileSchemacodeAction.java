package info.teksol.mindcode.cmdline;

import info.teksol.mindcode.InputFile;
import info.teksol.mindcode.InputPosition;
import info.teksol.mindcode.ToolMessage;
import info.teksol.mindcode.cmdline.Main.Action;
import info.teksol.mindcode.compiler.CompilerOutput;
import info.teksol.mindcode.compiler.CompilerProfile;
import info.teksol.schemacode.SchemacodeCompiler;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.impl.type.FileArgumentType;
import net.sourceforge.argparse4j.inf.ArgumentGroup;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.function.Function;

public class CompileSchemacodeAction extends ActionHandler {

    @Override
    Subparser appendSubparser(Subparsers subparsers, FileArgumentType inputFileType, CompilerProfile defaults) {
        Subparser subparser = subparsers.addParser(Action.COMPILE_SCHEMA.getShortcut())
                .aliases("compile-schema", "compile-schematic")
                .description("Compile a schematic definition file into binary msch file.")
                .help("Compile a schematic definition file into binary msch file.");

        subparser.addArgument("-c", "--clipboard")
                .help("encode the created schematic into text representation and paste into clipboard")
                .action(Arguments.storeTrue());

        ArgumentGroup files = subparser.addArgumentGroup("input/output files");

        files.addArgument("input")
                .help("Schematic definition file to be compiled into a binary msch file.")
                .nargs("?")
                .type(inputFileType.acceptSystemIn())
                .setDefault(new File("-"));

        files.addArgument("output")
                .help("Output file to receive the resulting binary Mindustry schematic file (.msch).")
                .nargs("?")
                .type(Arguments.fileType().verifyCanCreate());

        files.addArgument("-l", "--log")
                .help("output file to receive compiler messages; uses stdout/stderr when not specified")
                .nargs("?")
                .type(Arguments.fileType().verifyCanCreate())
                .setDefault(new File("-"));

        ArgumentGroup schematics = subparser.addArgumentGroup("schematic creation");

        schematics.addArgument("-a", "--add-tag")
                .help("defines additional tag(s) to add to the schematic, plain text and symbolic icon names are supported")
                .metavar("TAG")
                .type(String.class)
                .nargs("+")
                .setDefault(List.of());

        addAllCompilerOptions(subparser, defaults);

        return subparser;
    }

    @Override
    void handle(Namespace arguments) {
        CompilerProfile compilerProfile = createCompilerProfile(arguments);
        compilerProfile.setAdditionalTags(arguments.get("add_tag"));
        final File file = arguments.get("input");
        final InputFile inputFile = readFile(file, true);
        final Path basePath = isStdInOut(file) ? Paths.get("") : file.toPath().toAbsolutePath().getParent();

        final CompilerOutput<byte[]> result = SchemacodeCompiler.compile(inputFile , compilerProfile, basePath);

        final File output = resolveOutputFile(file, arguments.get("output"), ".msch");
        final File logFile = resolveOutputFile(file, arguments.get("log"), ".log");
        final boolean mlogToStdErr = isStdInOut(output);
        final Function<InputPosition, String> positionFormatter = InputPosition::formatForIde;

        if (!result.hasErrors()) {
            writeOutput(output, result.output());

            if (arguments.getBoolean("clipboard")) {
                writeToClipboard(Base64.getEncoder().encodeToString(result.output()));
                result.addMessage(ToolMessage.info("\nCreated schematic was copied to the clipboard."));
            }

            outputMessages(result, output, logFile, positionFormatter);
        } else {
            // Errors: print just them into stderr
            List<String> errors = result.errors(m -> m.formatMessage(positionFormatter));

            errors.forEach(System.err::println);
            if (!isStdInOut(logFile)) {
                writeOutput(logFile, errors, true);
            }
            System.exit(1);
        }
    }
}

