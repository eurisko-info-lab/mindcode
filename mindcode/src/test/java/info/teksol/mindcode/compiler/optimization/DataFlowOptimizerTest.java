package info.teksol.mindcode.compiler.optimization;

import info.teksol.mindcode.compiler.CompilerProfile;
import info.teksol.mindcode.compiler.GenerationGoal;
import org.junit.jupiter.api.Test;

import java.util.List;

import static info.teksol.mindcode.logic.Opcode.*;
import static junit.framework.Assert.assertEquals;

class DataFlowOptimizerTest extends AbstractOptimizerTest<DataFlowOptimizer> {

    @Override
    protected Class<DataFlowOptimizer> getTestedClass() {
        return DataFlowOptimizer.class;
    }

    @Override
    protected List<Optimization> getAllOptimizations() {
        return List.of(
                Optimization.DEAD_CODE_ELIMINATION,
                Optimization.CONDITIONAL_JUMPS_IMPROVEMENT,
                Optimization.SINGLE_STEP_JUMP_ELIMINATION,
                Optimization.JUMP_OVER_JUMP_ELIMINATION,
                Optimization.UNREACHABLE_CODE_ELIMINATION,
                Optimization.INPUT_TEMPS_ELIMINATION,
                Optimization.OUTPUT_TEMPS_ELIMINATION,
                Optimization.LOOP_OPTIMIZATION,
                Optimization.IF_EXPRESSION_OPTIMIZATION,
                Optimization.DATA_FLOW_OPTIMIZATION
        );
    }

    @Override
    protected CompilerProfile createCompilerProfile() {
        return super.createCompilerProfile().setGoal(GenerationGoal.SPEED);
    }

    @Test
    void removesUnneededAssignments() {
        assertCompilesTo("""
                        i = 5
                        j = 10
                        i = j + 1
                        j = 15
                        i = j + 2
                        print(i, j)
                        """,
                createInstruction(PRINT, "17"),
                createInstruction(PRINT, "15"),
                createInstruction(END)
        );
    }


    @Test
    void leavesUninitializedVariables() {
        assertCompilesTo("""
                        a = 0
                        if b
                            a = 1
                            b = 1
                        end
                        b = 2
                        print(a)
                        """,
                createInstruction(SET, "a", "0"),
                createInstruction(JUMP, var(1000), "equal", "b", "false"),
                createInstruction(SET, "a", "1"),
                createInstruction(LABEL, var(1000)),
                createInstruction(LABEL, var(1001)),
                createInstruction(SET, "b", "2"),
                createInstruction(PRINT, "a"),
                createInstruction(END)
        );
    }

    @Test
    void generatesUninitializedWarning() {
        generateInstructions("""
                j = i
                i = 10      // Avoid the warning generated by dead code eliminator
                print(i, j)
                """);

        assertEquals(
                """
                        List of uninitialized variables: i.""",
                extractWarnings(messages)
        );
    }

    @Test
    void recognizesIfStatementInitialization() {
        generateInstructions("""
                if switch1.enabled
                    a = 1
                    b = a
                else
                    a = 2
                    b = a
                end
                print(a, b)
                """);

        assertEquals("", extractWarnings(messages));
    }

    @Test
    void recognizesPartialIfStatementInitialization() {
        generateInstructions("""
                if switch1.enabled
                    a = 1
                    b = a
                else
                    a = 2
                    print(a)
                end
                print(b)
                """);

        assertEquals(
                """
                        List of uninitialized variables: b.""",
                extractWarnings(messages)
        );
    }

    @Test
    void processesPartialIfStatementInitialization() {
        assertCompilesTo("""
                        a = 0
                        b = 0
                        if switch1.enabled
                            a = 1
                            b = 2
                        else
                            a = 2
                            b = 3
                            print(a)
                        end
                        print(b)
                        """,
                createInstruction(SENSOR, var(0), "switch1", "@enabled"),
                createInstruction(JUMP, var(1000), "equal", var(0), "false"),
                createInstruction(SET, "b", "2"),
                createInstruction(JUMP, var(1001), "always"),
                createInstruction(LABEL, var(1000)),
                createInstruction(SET, "b", "3"),
                createInstruction(PRINT, "2"),
                createInstruction(LABEL, var(1001)),
                createInstruction(PRINT, "b"),
                createInstruction(END)
        );
    }

    @Test
    void removesUnneededAssignmentsInConditions() {
        assertCompilesTo("""
                        a = 0
                        if switch1.enabled
                            a = 1
                            b = a
                        else
                            a = 2
                            b = 1
                        end
                        print(a, b)
                        """,
                createInstruction(SENSOR, var(0), "switch1", "@enabled"),
                createInstruction(JUMP, var(1000), "equal", var(0), "false"),
                createInstruction(SET, "a", "1"),
                createInstruction(JUMP, var(1001), "always"),
                createInstruction(LABEL, var(1000)),
                createInstruction(SET, "a", "2"),
                createInstruction(LABEL, var(1001)),
                createInstruction(PRINT, "a"),
                createInstruction(PRINT, "1"),
                createInstruction(END)
        );
    }

    @Test
    void handlesWhileLoops() {
        assertCompilesTo("""
                        i = 0
                        while i < 10
                            i = i + 1
                        end
                        print(i)
                        """,
                createInstruction(SET, "i", "0"),
                createInstruction(LABEL, var(1000)),
                createInstruction(LABEL, var(1003)),
                createInstruction(OP, "add", "i", "i", "1"),
                createInstruction(LABEL, var(1001)),
                createInstruction(JUMP, var(1003), "lessThan", "i", "10"),
                createInstruction(LABEL, var(1002)),
                createInstruction(PRINT, "i"),
                createInstruction(END)
        );
    }

    @Test
    void handlesRangedForLoops() {
        assertCompilesTo("""
                        for i in 0 ... SIZE - 1
                            min = cell1[i]
                        end
                        """,
                createInstruction(OP, "sub", var(1), "SIZE", "1"),
                createInstruction(SET, "i", "0"),
                createInstruction(LABEL, var(1000)),
                createInstruction(JUMP, var(1002), "greaterThanEq", "0", var(1)),
                createInstruction(LABEL, var(1001)),
                createInstruction(OP, "add", "i", "i", "1"),
                createInstruction(JUMP, var(1001), "lessThan", "i", var(1)),
                createInstruction(LABEL, var(1002)),
                createInstruction(END)
        );
    }

    @Test
    void handlesSingleBranchIfStatements() {
        assertCompilesTo("""
                        a = 1
                        if switch1.enabled
                            a = 2
                        end
                        print(a)
                        """,
                createInstruction(SET, "a", "1"),
                createInstruction(SENSOR, var(0), "switch1", "@enabled"),
                createInstruction(JUMP, var(1000), "equal", var(0), "false"),
                createInstruction(SET, "a", "2"),
                createInstruction(LABEL, var(1000)),
                createInstruction(LABEL, var(1001)),
                createInstruction(PRINT, "a"),
                createInstruction(END)
        );
    }

    @Test
    void handlesOptimizedIfStatements() {
        assertCompilesTo("""
                        a = 10
                        print(a > 5 ? "High" : "Low")
                        """,
                createInstruction(SET, var(1), q("Low")),
                createInstruction(JUMP, var(1001), "lessThanEq", "10", "5"),
                createInstruction(SET, var(1), q("High")),
                createInstruction(LABEL, var(1001)),
                createInstruction(PRINT, var(1)),
                createInstruction(END)
        );
    }

    @Test
    void handlesReturnStatements() {
        assertCompilesTo("""
                        inline def foo(n)
                          return n
                        end
                                        
                        print(foo(4))
                        """,
                createInstruction(LABEL, var(1000)),
                createInstruction(SET, var(0), "4"),
                createInstruction(LABEL, var(1001)),
                createInstruction(PRINT, var(0)),
                createInstruction(END)
        );
    }


    @Test
    void handlesCaseExpressions() {
        assertCompilesTo("""
                        a = case cell1[0]
                            when 0, 1, 2 then 10
                            when 10 .. 20 then 20
                            else 30
                        end
                        print(a)
                        """,
                createInstruction(READ, "__ast0", "cell1", "0"),
                createInstruction(JUMP, var(1002), "equal", "__ast0", "0"),
                createInstruction(JUMP, var(1002), "equal", "__ast0", "1"),
                createInstruction(JUMP, var(1001), "notEqual", "__ast0", "2"),
                createInstruction(LABEL, var(1002)),
                createInstruction(SET, var(1), "10"),
                createInstruction(JUMP, var(1000), "always"),
                createInstruction(LABEL, var(1001)),
                createInstruction(JUMP, var(1005), "lessThan", "__ast0", "10"),
                createInstruction(JUMP, var(1004), "lessThanEq", "__ast0", "20"),
                createInstruction(LABEL, var(1005)),
                createInstruction(JUMP, var(1003), "always"),
                createInstruction(LABEL, var(1004)),
                createInstruction(SET, var(1), "20"),
                createInstruction(JUMP, var(1000), "always"),
                createInstruction(LABEL, var(1003)),
                createInstruction(SET, var(1), "30"),
                createInstruction(LABEL, var(1000)),
                createInstruction(SET, "a", var(1)),
                createInstruction(PRINT, "a"),
                createInstruction(END)
        );
    }

    @Test
    void handlesFunctionArgumentSetup() {
        assertCompilesTo("""
                        def getBit(bitIndex)
                          bitIndex * 2
                        end
                                                
                        for n in 1 .. 10
                            print(getBit(n \\ 2))
                        end
                        getBit(0)
                        """,
                createInstruction(SET, "n", "1"),
                createInstruction(LABEL, var(1001)),
                createInstruction(LABEL, var(1007)),
                createInstruction(OP, "idiv", "__fn0_bitIndex", "n", "2"),
                createInstruction(SETADDR, "__fn0retaddr", var(1004)),
                createInstruction(CALL, var(1000)),
                createInstruction(GOTOLABEL, var(1004), "__fn0"),
                createInstruction(SET, var(1), "__fn0retval"),
                createInstruction(PRINT, var(1)),
                createInstruction(LABEL, var(1002)),
                createInstruction(OP, "add", "n", "n", "1"),
                createInstruction(JUMP, var(1007), "lessThanEq", "n", "10"),
                createInstruction(LABEL, var(1003)),
                createInstruction(SET, "__fn0_bitIndex", "0"),
                createInstruction(SETADDR, "__fn0retaddr", var(1005)),
                createInstruction(CALL, var(1000)),
                createInstruction(GOTOLABEL, var(1005), "__fn0"),
                createInstruction(END),
                createInstruction(LABEL, var(1000)),
                createInstruction(OP, "mul", "__fn0retval", "__fn0_bitIndex", "2"),
                createInstruction(LABEL, var(1006)),
                createInstruction(GOTO, "__fn0retaddr", "__fn0"),
                createInstruction(END)
        );
    }
}