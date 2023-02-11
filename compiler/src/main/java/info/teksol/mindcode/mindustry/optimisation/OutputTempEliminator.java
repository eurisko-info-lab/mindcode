package info.teksol.mindcode.mindustry.optimisation;

import info.teksol.mindcode.mindustry.instructions.LogicInstruction;
import info.teksol.mindcode.mindustry.generator.LogicInstructionGenerator;
import info.teksol.mindcode.mindustry.LogicInstructionPipeline;
import info.teksol.mindcode.mindustry.instructions.InstructionProcessor;
import java.util.List;

// Generic optimizer to remove all assignments to temporary variables that carry over the output value
// of the preceding instruction. The set instruction is removed, while the preceding instruction is updated
// to replace the temp variable with the target variable used in the set statement.
// The optimization is performed only when the following conditions are met:
// 1. The set instruction assigns from a __tmp variable.
// 2. The __tmp variable is used in exactly one other instruction. If that instruction is a set,
//    it sets (produces) the __tmp variable in question.
// 3. The set instruction immediatelly follows the instruction producing the __tmp variable 
//    (the check is based on absolute instruction sequence in the program, not on the actual program flow).
// 4. All arguments of the other instruction referencing the __tmp variable are output ones.
class OutputTempEliminator extends GlobalOptimizer {
    public OutputTempEliminator(InstructionProcessor instructionProcessor, LogicInstructionPipeline next) {
        super(instructionProcessor, next);
    }

    @Override
    protected boolean optimizeProgram() {
        // Cannot uswe iterations due to modifications of the the underlying list in the loop
        for (int index  = 1; index < program.size(); index++)  {
            LogicInstruction current = program.get(index);
            if (!current.isSet()) continue;

            String arg1 = current.getArgs().get(1);
            // Not an assignment from a temp variable
            if (!arg1.startsWith(LogicInstructionGenerator.TMP_PREFIX)) continue;
            
            LogicInstruction previous = program.get(index - 1);
            // Previous instruction a set producing a different variable
            if (previous.isSet() && !previous.getArgs().get(0).equals(arg1)) continue;
            
            List<LogicInstruction> list = findInstructions(ix -> ix.getArgs().contains(arg1));
            // Not exactly two instructions, or the previous instruction doesn't produce the tmp variable
            if (list.size() != 2 || list.get(0) != previous) continue;

            // Make sure all arg0 arguments of the other instruction are input
            boolean replacesOutputArg = previous.getTypedArguments()
                    .filter(t -> t.getValue().equals(arg1))
                    .allMatch(t -> t.getArgumentType().isOutput());
            if (!replacesOutputArg) continue;

            // The current instruction merely transfers a value from the output argument of the previous instruction
            // Replacing instruction argument by value
            String arg0 = current.getArgs().get(0);
            program.set(index - 1, replaceAllArgs(previous, arg1, arg0));
            program.remove(index);
            index--;
        }

        return false;
    }
}
