package info.teksol.mindcode.compiler;

import info.teksol.mindcode.MindcodeInternalError;
import info.teksol.mindcode.compiler.instructions.*;
import info.teksol.mindcode.logic.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static info.teksol.mindcode.logic.Opcode.OP;

public class LogicInstructionLabelResolver {
    private final CompilerProfile profile;
    private final InstructionProcessor instructionProcessor;

    private final Map<LogicLabel, LogicLabel> addresses = new HashMap<>();

    public LogicInstructionLabelResolver(InstructionProcessor instructionProcessor, CompilerProfile profile) {
        this.instructionProcessor = instructionProcessor;
        this.profile = profile;
    }
    
    public static List<LogicInstruction> resolve(InstructionProcessor instructionProcessor, CompilerProfile profile,
            List<LogicInstruction> program) {
        return new LogicInstructionLabelResolver(instructionProcessor, profile).resolve(program);
    }

    private List<LogicInstruction> resolve(List<LogicInstruction> program) {
        program = resolveRemarks(program);
        calculateAddresses(program);
        return resolveAddresses(resolveVirtualInstructions(program));
    }

    private List<LogicInstruction> resolveRemarks(List<LogicInstruction> program) {
        return switch (profile.getRemarks()) {
            case ACTIVE     -> resolveRemarksActive(program);
            case NONE       -> resolveRemarksNone(program);
            case PASSIVE    -> resolveRemarksPassive(program);
        };
    }

    private List<LogicInstruction> resolveRemarksActive(List<LogicInstruction> program) {
        return program.stream()
                .map(ix -> ix instanceof RemarkInstruction r ? instructionProcessor.createPrint(r.getAstContext(), r.getValue()) : ix)
                .toList();
    }

    private List<LogicInstruction> resolveRemarksNone(List<LogicInstruction> program) {
        return program.stream()
                .filter(ix -> !(ix instanceof RemarkInstruction))
                .toList();
    }

    private List<LogicInstruction> resolveRemarksPassive(List<LogicInstruction> program) {
        List<LogicInstruction> result = new ArrayList<>();
        LabelInstruction activeLabel = null;
        for (LogicInstruction ix : program) {
            if (ix instanceof RemarkInstruction) {
                if (activeLabel == null) {
                    LogicLabel label = instructionProcessor.nextLabel();
                    activeLabel = instructionProcessor.createLabel(ix.getAstContext(),label);
                    result.add(instructionProcessor.createJumpUnconditional(ix.getAstContext(), label));
                }
                result.add(instructionProcessor.createPrint(ix.getAstContext(), ((RemarkInstruction) ix).getValue()));
            } else {
                if (activeLabel != null) {
                    result.add(activeLabel);
                    activeLabel = null;
                }
                result.add(ix);
            }
        }

        return result;
    }

    private void calculateAddresses(List<LogicInstruction> program) {
        int instructionPointer = 0;
        for (int i = 0; i < program.size(); i++) {
            final LogicInstruction instruction = program.get(i);
            instructionPointer += instruction.getRealSize();
            if (instruction instanceof LabeledInstruction ix) {
                if (addresses.containsKey(ix.getLabel())) {
                    throw new MindcodeInternalError("Duplicate label detected: '%s' reused at least twice in %s", ix.getLabel(), program);
                }

                addresses.put(ix.getLabel(), LogicLabel.absolute(instructionPointer));
            }
        }
    }

    private LogicArgument resolveLabel(LogicArgument argument) {
        if (argument instanceof LogicLabel label) {
            if (!addresses.containsKey(label)) {
                throw new MindcodeInternalError("Unknown jump label target: '%s' was not previously discovered in program.", label);
            }
            return addresses.get(label);
        } else {
            return argument;
        }
    }

    private List<LogicInstruction> resolveAddresses(List<LogicInstruction> program) {
        final List<LogicInstruction> result = new ArrayList<>();

        for (final LogicInstruction instruction : program) {
            if (instruction instanceof GotoOffsetInstruction ix) {
                if (resolveLabel(ix.getTarget()) instanceof LogicLabel label && label.getAddress() >= 0) {
                    int offset = label.getAddress() - ix.getOffset().getIntValue();
                    LogicInstruction newInstruction = instructionProcessor.createInstruction(ix.getAstContext(),
                            OP, Operation.ADD, LogicBuiltIn.COUNTER, ix.getValue(), LogicNumber.get(offset));
                    result.add(newInstruction);
                } else {
                    throw new MindcodeInternalError("GotoOffset target '%s' is not a label.", ix.getTarget());
                }
            } else if (instruction.getArgs().stream().anyMatch(a -> a.getType() == ArgumentType.LABEL)) {
                List<LogicArgument> newArgs = instruction.getArgs().stream().map(this::resolveLabel).toList();
                LogicInstruction newInstruction = instructionProcessor.replaceArgs(instruction, newArgs);
                result.add(newInstruction);
            } else {
                result.add(instruction);
            }
        }
        return result;
    }

    private List<LogicInstruction> resolveVirtualInstructions(List<LogicInstruction> program) {
        return program.stream().mapMulti(instructionProcessor::resolve).toList();
    }
}
