package info.teksol.mindcode.mindustry.optimisation;

import info.teksol.mindcode.mindustry.instructions.LogicInstruction;
import info.teksol.mindcode.mindustry.LogicInstructionPipeline;
import java.util.ArrayList;
import java.util.List;

// 1. Remove jumps (both conditional and unconditional) that target the next instruction.
//    Technically, if we have a sequence
//      0: jump 2 ...
//      1: jump 2 ...
//      2: ...
//    we could eliminate both jumps. This class will only remove the second jump, because before that removal the first
//    one doesn't target the next instruction. However, such sequences aren't typically generated by the compiler.
class SingleStepJumpEliminator extends PipelinedOptimizer {
    SingleStepJumpEliminator(LogicInstructionPipeline next) {
        super(next);
    }

    @Override
    protected State initialState() {
        return new EmptyState();
    }

    private final class EmptyState implements State {
        @Override
        public State emit(LogicInstruction instruction) {
            if (instruction.isJump()) {
                return new ExpectLabel(instruction);
            } else {
                emitToNext(instruction);
                return this;
            }
        }

        @Override
        public State flush() {
            return this;
        }
    }

    private final class ExpectLabel implements State {
        private final LogicInstruction jump;
        private final String targetLabel;
        private final List<LogicInstruction> labels = new ArrayList<>();
        private boolean isJumpToNext = false;

        ExpectLabel(LogicInstruction jump) {
            this.jump = jump;
            this.targetLabel = jump.getArgs().get(0);
        }

        @Override
        public State emit(LogicInstruction instruction) {
            if (instruction.isLabel()) {
                if (instruction.getArgs().get(0).equals(targetLabel)) {
                    isJumpToNext = true;
                }
                labels.add(instruction);
                return this;
            }
            
            if (!isJumpToNext) {
                emitToNext(jump);
            }
            labels.forEach(SingleStepJumpEliminator.this::emitToNext);
            
            if (instruction.isJump()) {
                return new ExpectLabel(instruction);
            } else {
                emitToNext(instruction);
                return new EmptyState();
            }
        }

        @Override
        public State flush() {
            emitToNext(jump);
            labels.forEach(SingleStepJumpEliminator.this::emitToNext);
            return new EmptyState();
        }
    }
}
