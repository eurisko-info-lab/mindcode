package info.teksol.mindcode.compiler.optimization;

import info.teksol.mindcode.compiler.CompilerProfile;
import info.teksol.mindcode.compiler.GenerationGoal;
import org.junit.jupiter.api.Test;

import java.util.List;

import static info.teksol.mindcode.logic.Opcode.*;

class FunctionInlinerTest extends AbstractOptimizerTest<FunctionInliner> {

    @Override
    protected Class<FunctionInliner> getTestedClass() {
        return FunctionInliner.class;
    }

    @Override
    protected List<Optimization> getAllOptimizations() {
        return Optimization.LIST;
    }

    @Override
    protected CompilerProfile createCompilerProfile() {
        return super.createCompilerProfile().setGoal(GenerationGoal.SPEED);
    }

    @Test
    void inlinesFunction() {
        assertCompilesTo("""
                        def foo(n)
                            print(2 * n);
                        end;

                        foo(1);
                        foo(2);
                        """,
                createInstruction(PRINT, q("24"))
        );
    }

    @Test
    void inlinesTwoFunction() {
        assertCompilesTo("""
                        def foo(n)
                            print(2 * n);
                        end;

                        def bar(n)
                            foo(n);
                            foo(n + 1);
                        end;

                        bar(1);
                        bar(3);
                        """,
                createInstruction(PRINT, q("2468"))
        );
    }

    @Test
    void inlinesFunctionInsideLoop() {
        assertCompilesTo("""
                        def foo(n)
                            print(n / 2);
                            sum = 0;
                            for i in 0 .. n do
                                sum += i;
                            end;
                            return sum;
                        end;

                        for i in 0 ... 10 do
                            foo(2 * i);
                        end;
                        foo(0);
                        """,
                createInstruction(PRINT, q("01234567890"))
        );
    }

    @Test
    void inlinesTwoFunctionCallsInsideLoop() {
        CompilerProfile compilerProfile = createCompilerProfile().setAllOptimizationLevels(OptimizationLevel.BASIC);
        TestCompiler compiler = createTestCompiler(compilerProfile);
        assertCompilesTo(compiler,
                        """
                        while true do
                            a = foo();
                            b = foo();
                            print(a, b);
                        end;
                        
                        def foo()
                            rand(10);
                        end;
                        """,
                createInstruction(LABEL, var(1001)),
                createInstruction(OP, "rand", "__fn0retval", "10"),
                createInstruction(SET, "a", "__fn0retval"),
                createInstruction(OP, "rand", "__fn0retval", "10"),
                createInstruction(SET, "b", "__fn0retval"),
                createInstruction(PRINT, "a"),
                createInstruction(PRINT, "__fn0retval"),
                createInstruction(JUMP, var(1001), "always"),
                createInstruction(END)
        );
    }

    @Test
    void inlinesNestedFunctionCalls() {
        assertCompilesTo("""
                        def foo(n)
                            print(n + 1);
                        end;

                        foo(foo(1));
                        """,
                createInstruction(PRINT, q("23"))
        );
    }

    @Test
    void inlinesFunctionCallsInExpressions() {
        assertCompilesTo("""
                        def foo()
                            rand(10);
                        end;
                        print(foo() + foo());
                        """,
                createInstruction(OP, "rand", "__fn0retval", "10"),
                createInstruction(SET, var(0), "__fn0retval"),
                createInstruction(OP, "rand", "__fn0retval", "10"),
                createInstruction(OP, "add", var(2), var(0), "__fn0retval"),
                createInstruction(PRINT, var(2))
        );
    }


    @Test
    void inlinesFunctionCallsWithParametersInExpressions() {
        assertCompilesTo("""
                        def foo(n)
                            t = rand(n);
                            return t;
                        end;
                        print(foo(10) + foo(20));
                        """,
                createInstruction(OP, "rand", "__fn0_t", "10"),
                createInstruction(SET, var(0), "__fn0_t"),
                createInstruction(OP, "rand", "__fn0_t", "20"),
                createInstruction(OP, "add", var(2), var(0), "__fn0_t"),
                createInstruction(PRINT, var(2))
        );
    }

    @Test
    void handlesReturnsCorrectly() {
        assertCompilesTo("""
                        def foo()
                            t = rand(10);
                            return t;
                        end;
                        print(foo());
                        print(foo());
                        """,
                createInstruction(OP, "rand", "__fn0_t", "10"),
                createInstruction(PRINT, "__fn0_t"),
                createInstruction(OP, "rand", "__fn0_t", "10"),
                createInstruction(PRINT, "__fn0_t")
        );
    }

    @Test
    void respectsNoinlineFunctions() {
        assertCompilesTo("""
                        noinline def foo(n)
                            print(2 * n);
                        end;

                        foo(1);
                        foo(2);
                        """,
                createInstruction(SET, "__fn0_n", "1"),
                createInstruction(SETADDR, "__fn0retaddr", var(1001)),
                createInstruction(CALL, var(1000), "__fn0retval"),
                createInstruction(GOTOLABEL, var(1001), "__fn0"),
                createInstruction(SET, "__fn0_n", "2"),
                createInstruction(SETADDR, "__fn0retaddr", var(1002)),
                createInstruction(CALL, var(1000), "__fn0retval"),
                createInstruction(GOTOLABEL, var(1002), "__fn0"),
                createInstruction(END),
                createInstruction(LABEL, var(1000)),
                createInstruction(OP, "mul", var(2), "2", "__fn0_n"),
                createInstruction(PRINT, var(2)),
                createInstruction(GOTO, "__fn0retaddr", "__fn0")
        );
    }

}