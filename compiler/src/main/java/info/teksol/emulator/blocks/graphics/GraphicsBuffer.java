package info.teksol.emulator.blocks.graphics;

import info.teksol.emulator.processor.ExecutionException;
import info.teksol.emulator.processor.ExecutionFlag;
import info.teksol.mindcode.compiler.instructions.DrawInstruction;

import java.util.ArrayList;
import java.util.List;

public class GraphicsBuffer {
    private final int sizeLimit;
    private final List<DrawInstruction> drawInstructions = new ArrayList<>();

    // In Mindustry Logic, the transformation matrix is owned by the processor and is not reset when the
    // processor's code is recompiled.
    // Here we want to reset the matrix for each independent run
    private final TransformationMatrix transformationMatrix = new TransformationMatrix();

    public GraphicsBuffer(int sizeLimit) {
        this.sizeLimit = sizeLimit;
    }

    public boolean draw(DrawInstruction instruction) {
        drawInstructions.add(instruction);

        // Only report the error once
        if (drawInstructions.size() == sizeLimit + 1) {
            throw new ExecutionException(ExecutionFlag.ERR_GRAPHICS_BUFFER_OVERFLOW, "Capacity of the graphics buffer (%d) exceeded.", sizeLimit);
        }

        return true;
    }

    public List<DrawInstruction> getDrawInstructions() {
        return drawInstructions;
    }

    public TransformationMatrix getTransformationMatrix() {
        return transformationMatrix;
    }

    public void clear() {
        drawInstructions.clear();
    }
}
