package info.teksol.mindcode.mindustry.generator;

import info.teksol.mindcode.ast.Seq;
import info.teksol.mindcode.mindustry.AbstractGeneratorTest;
import java.util.List;
import org.junit.jupiter.api.Test;

import static info.teksol.mindcode.mindustry.logic.Opcode.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

// This class tests generation of user-defined function and function calls.
// Original tests from LogicInstructionGeneratorTest class were moved here, although with the new function
// generation mechanism and automatic inlining the generated code is vastly different now.
// New tests suited to the new code generation were added.
public class LogicInstructionGeneratorFunctionsTest extends AbstractGeneratorTest {

    @Test
    void generatesImplicitInlineFunctions() {
        assertLogicInstructionsMatch(
                List.of(
                        createInstruction(SET, var(0), "3"),
                        createInstruction(LABEL, var(1010)),
                        createInstruction(SET, "__fn0_n", var(0)),
                        createInstruction(SET, var(1), "__fn0_n"),
                        createInstruction(LABEL, var(1000)),
                        createInstruction(PRINT, var(1)),
                        createInstruction(END)
                ),
                generateUnoptimized(
                        (Seq) translateToAst(""
                                + "def foo(n) "
                                + "  n "
                                + "end "
                                + "print(foo(3))"
                        )
                )
        );
    }

    @Test
    void generatesStacklessFunctions() {
        assertLogicInstructionsMatch(
                List.of(
                        createInstruction(SET, var(0), "3"),
                        createInstruction(SET, "__fn0_n", var(0)),
                        createInstruction(SET, "__fn0retaddr", var(1001)),
                        createInstruction(SET, "@counter", var(1000)),
                        createInstruction(LABEL, var(1001)),
                        createInstruction(SET, var(1), "__fn0retval"),
                        createInstruction(PRINT, var(1)),
                        createInstruction(SET, var(2), "4"),
                        createInstruction(SET, "__fn0_n", var(2)),
                        createInstruction(SET, "__fn0retaddr", var(1002)),
                        createInstruction(SET, "@counter", var(1000)),
                        createInstruction(LABEL, var(1002)),
                        createInstruction(SET, var(3), "__fn0retval"),
                        createInstruction(PRINT, var(3)),
                        createInstruction(END),
                        createInstruction(LABEL, var(1000)),
                        createInstruction(SET, "__fn0retval", "__fn0_n"),
                        createInstruction(LABEL, var(1003)),
                        createInstruction(SET, "@counter", "__fn0retaddr"),
                        createInstruction(END)
                ),
                generateUnoptimized(
                        (Seq) translateToAst(""
                                + "def foo(n) "
                                + "  n "
                                + "end "
                                + "print(foo(3)) "
                                + "print(foo(4))"
                        )
                )
        );
    }

    @Test
    void generatesExplicitInlineFunctions() {
        assertLogicInstructionsMatch(
                List.of(
                        createInstruction(SET, var(0), "3"),
                        createInstruction(LABEL, var(1010)),
                        createInstruction(SET, "__fn0_n", var(0)),
                        createInstruction(SET, var(1), "__fn0_n"),
                        createInstruction(LABEL, var(1000)),
                        createInstruction(PRINT, var(1)),
                        createInstruction(SET, var(2), "4"),
                        createInstruction(LABEL, var(1012)),
                        createInstruction(SET, "__fn1_n", var(2)),
                        createInstruction(SET, var(3), "__fn1_n"),
                        createInstruction(LABEL, var(1001)),
                        createInstruction(PRINT, var(3)),
                        createInstruction(END)
                ),
                generateUnoptimized(
                        (Seq) translateToAst(""
                                + "inline def foo(n) "
                                + "  n "
                                + "end "
                                + "print(foo(3)) "
                                + "print(foo(4))"
                        )
                )
        );
    }

    @Test
    void generatesRecursiveFunctions() {
        assertLogicInstructionsMatch(
                List.of(
                        createInstruction(SET, "__sp", "511"),
                        createInstruction(SET, var(0), "3"),
                        createInstruction(SET, "__fn0_n", var(0)),
                        createInstruction(CALL, "bank1", var(1000), var(1001)),
                        createInstruction(LABEL, var(1001)),
                        createInstruction(SET, var(1), "__fn0retval"),
                        createInstruction(PRINT, var(1)),
                        createInstruction(END),
                        createInstruction(LABEL, var(1000)),
                        createInstruction(PUSH, "bank1", "__fn0_n"),
                        createInstruction(SET, "__fn0_n", "__fn0_n"),
                        createInstruction(CALL, "bank1", var(1000), var(1002)),
                        createInstruction(LABEL, var(1002)),
                        createInstruction(POP, "bank1", "__fn0_n"),
                        createInstruction(SET, var(2), "__fn0retval"),
                        createInstruction(SET, "__fn0retval", var(2)),
                        createInstruction(LABEL, var(1003)),
                        createInstruction(RETURN, "bank1"),
                        createInstruction(END)
                ),
                generateUnoptimized(
                        (Seq) translateToAst(""
                                + "allocate stack in bank1[0...512] "
                                + "def foo(n) "
                                + "  foo(n)   "
                                + "end "
                                + "print(foo(3))"
                        )
                )
        );
    }

    @Test
    void generatesIndirectRecursion() {
        assertLogicInstructionsMatch(
                List.of(
                        createInstruction(SET, "__sp", "511"),
                        createInstruction(SET, var(0), "4"),
                        // call foo
                        createInstruction(SET, "__fn1_n", var(0)),
                        createInstruction(CALL, "bank1", var(1001), var(1002)),
                        createInstruction(LABEL, var(1002)),
                        createInstruction(SET, var(1), "__fn1retval"),
                        createInstruction(PRINT, var(1)),
                        createInstruction(END),
                        // def foo
                        createInstruction(LABEL, var(1000)),
                        createInstruction(SET, var(2), "1"),
                        // call bar
                        createInstruction(PUSH, "bank1", "__fn0_n"),
                        createInstruction(PUSH, "bank1", var(2)),
                        createInstruction(SET, "__fn1_n", "__fn0_n"),
                        createInstruction(CALL, "bank1", var(1001), var(1003)),
                        createInstruction(LABEL, var(1003)),
                        createInstruction(POP, "bank1", var(2)),
                        createInstruction(POP, "bank1", "__fn0_n"),
                        createInstruction(SET, var(3), "__fn1retval"),
                        createInstruction(OP, "sub", var(4), var(2), var(3)),
                        createInstruction(SET, "__fn0retval", var(4)),
                        createInstruction(LABEL, var(1005)),
                        createInstruction(RETURN, "bank1"),
                        createInstruction(END),
                        // def bar
                        createInstruction(LABEL, var(1001)),
                        createInstruction(SET, var(5), "1"),
                        // call foo
                        createInstruction(PUSH, "bank1", "__fn1_n"),
                        createInstruction(PUSH, "bank1", var(5)),
                        createInstruction(SET, "__fn0_n", "__fn1_n"),
                        createInstruction(CALL, "bank1", var(1000), var(1004)),
                        createInstruction(LABEL, var(1004)),
                        createInstruction(POP, "bank1", var(5)),
                        createInstruction(POP, "bank1", "__fn1_n"),
                        createInstruction(SET, var(6), "__fn0retval"),
                        createInstruction(OP, "add", var(7), var(5), var(6)),
                        createInstruction(SET, "__fn1retval", var(7)),
                        createInstruction(LABEL, var(1006)),
                        createInstruction(RETURN, "bank1"),
                        createInstruction(END)
                ),
                generateUnoptimized(
                        (Seq) translateToAst(""
                                + "allocate stack in bank1[0...512] "
                                + "def foo(n) 1 + bar(n) end "
                                + "def bar(n) 1 - foo(n) end "
                                + "print(foo(4))"
                        )
                )
        );
    }

    @Test
    void passesParametersToInlineFunction() {
        assertLogicInstructionsMatch(
                List.of(
                        createInstruction(SET, var(0), "4"),
                        createInstruction(SET, "boo", var(0)),
                        createInstruction(SET, var(1), "3"),
                        createInstruction(LABEL, var(1010)),
                        createInstruction(SET, "__fn0_n", var(1)),
                        createInstruction(SET, "__fn0_r", "boo"),
                        createInstruction(SET, var(2), "2"),
                        createInstruction(OP, "pow", var(3), "__fn0_n", "__fn0_r"),
                        createInstruction(OP, "mul", var(4), var(2), var(3)),
                        createInstruction(SET, var(5), var(4)),
                        createInstruction(LABEL, var(1000)),
                        createInstruction(SET, "x", var(5)),
                        createInstruction(PRINT, "x"),
                        createInstruction(PRINTFLUSH, "message1"),
                        createInstruction(END)
                ),
                generateUnoptimized(
                        (Seq) translateToAst(""
                                + "def foo(n, r) "
                                + "2 * (n ** r) "
                                + "end "
                                + " "
                                + "boo = 4 "
                                + "x = foo(3, boo) "
                                + "print(x) "
                                + "printflush(message1)")
                )
        );
    }

    @Test
    void functionsCanCallOtherFunctionsInline() {
        assertLogicInstructionsMatch(
                List.of(
                        createInstruction(SET, var(0), "4"),
                        createInstruction(LABEL, var(1010)),
                        createInstruction(SET, "__fn0_n", var(0)),
                        createInstruction(SET, var(2), "1"),
                        createInstruction(LABEL, var(1012)),
                        createInstruction(SET, "__fn1_n", "__fn0_n"),
                        createInstruction(SET, var(4), "2"),
                        createInstruction(LABEL, var(1014)),
                        createInstruction(SET, "__fn2_n", "__fn1_n"),
                        createInstruction(SET, var(6), "3"),
                        createInstruction(OP, "pow", var(7), var(6), "__fn2_n"),
                        createInstruction(SET, var(5), var(7)),
                        createInstruction(LABEL, var(1002)),
                        createInstruction(OP, "mul", var(8), var(4), var(5)),
                        createInstruction(SET, var(3), var(8)),
                        createInstruction(LABEL, var(1001)),
                        createInstruction(OP, "add", var(9), var(2), var(3)),
                        createInstruction(SET, var(1), var(9)),
                        createInstruction(LABEL, var(1000)),
                        createInstruction(PRINT, var(1)),
                        createInstruction(END)
                ),
                generateUnoptimized(
                        (Seq) translateToAst(""
                                + "def foo(n) 1 + bar(n) end "
                                + "def bar(n) 2 * baz(n) end "
                                + "def baz(n) 3 ** n end "
                                + "print(foo(4))"
                        )
                )
        );
    }

    @Test
    void functionsCanCallOtherFunctionsStackless() {
        assertLogicInstructionsMatch(
                List.of(
                        // call foo
                        createInstruction(SET, var(0), "0"),
                        createInstruction(SET, "__fn1_n", var(0)),
                        createInstruction(SET, "__fn1retaddr", var(1003)),
                        createInstruction(SET, "@counter", var(1001)),
                        createInstruction(LABEL, var(1003)),
                        createInstruction(SET, var(1), "__fn1retval"),
                        // call bar
                        createInstruction(SET, var(2), "0"),
                        createInstruction(SET, "__fn0_n", var(2)),
                        createInstruction(SET, "__fn0retaddr", var(1004)),
                        createInstruction(SET, "@counter", var(1000)),
                        createInstruction(LABEL, var(1004)),
                        createInstruction(SET, var(3), "__fn0retval"),
                        // call baz
                        createInstruction(SET, var(4), "0"),
                        createInstruction(SET, "__fn2_n", var(4)),
                        createInstruction(SET, "__fn2retaddr", var(1005)),
                        createInstruction(SET, "@counter", var(1002)),
                        createInstruction(LABEL, var(1005)),
                        createInstruction(SET, var(5), "__fn2retval"),
                        // call foo (again)
                        createInstruction(SET, var(6), "4"),
                        createInstruction(SET, "__fn1_n", var(6)),
                        createInstruction(SET, "__fn1retaddr", var(1006)),
                        createInstruction(SET, "@counter", var(1001)),
                        createInstruction(LABEL, var(1006)),
                        createInstruction(SET, var(7), "__fn1retval"),
                        createInstruction(PRINT, var(7)),
                        createInstruction(END),
                        // def bar
                        createInstruction(LABEL, var(1000)),
                        createInstruction(SET, var(8), "2"),
                        // call baz
                        createInstruction(SET, "__fn2_n", "__fn0_n"),
                        createInstruction(SET, "__fn2retaddr", var(1007)),
                        createInstruction(SET, "@counter", var(1002)),
                        createInstruction(LABEL, var(1007)),
                        createInstruction(OP, "mul", var(9), var(8), "__fn2retval"),
                        createInstruction(SET, "__fn0retval", var(9)),
                        createInstruction(LABEL, var(1009)),
                        createInstruction(SET, "@counter", "__fn0retaddr"),
                        createInstruction(END),
                        // def foo
                        createInstruction(LABEL, var(1001)),
                        createInstruction(SET, var(10), "1"),
                        // call bar
                        createInstruction(SET, "__fn0_n", "__fn1_n"),
                        createInstruction(SET, "__fn0retaddr", var(1008)),
                        createInstruction(SET, "@counter", var(1000)),
                        createInstruction(LABEL, var(1008)),
                        createInstruction(OP, "add", var(11), var(10), "__fn0retval"),
                        createInstruction(SET, "__fn1retval", var(11)),
                        createInstruction(LABEL, var(1010)),
                        createInstruction(SET, "@counter", "__fn1retaddr"),
                        createInstruction(END),
                        // def baz
                        createInstruction(LABEL, var(1002)),
                        createInstruction(SET, var(12), "3"),
                        createInstruction(OP, "pow", var(13), var(12), "__fn2_n"),
                        createInstruction(SET, "__fn2retval", var(13)),
                        createInstruction(LABEL, var(1011)),
                        createInstruction(SET, "@counter", "__fn2retaddr"),
                        createInstruction(END)
                ),
                generateUnoptimized(
                        (Seq) translateToAst(""
                                + "def foo(n) 1 + bar(n) end "
                                + "def bar(n) 2 * baz(n) end "
                                + "def baz(n) 3 ** n end "
                                + "foo(0) "
                                + "bar(0) "
                                + "baz(0) "
                                + "print(foo(4))"
                        )
                )
        );
    }

    @Test
    void canIndirectlyReferenceStack() {
        assertLogicInstructionsMatch(
                List.of(
                        // Setting up stack
                        createInstruction(SET, "STACKPTR", "cell1"),
                        createInstruction(SET, "HEAPPTR", "cell2"),
                        createInstruction(SET, "__sp", "511"),
                        createInstruction(SENSOR, var(9), "STACKPTR", "@type"),
                        createInstruction(JUMP, var(1009), "equal", var(9), "@memory-bank"),
                        createInstruction(SET, "__sp", "63"),
                        createInstruction(LABEL, var(1009)),
                        // Function call
                        createInstruction(CALL, "STACKPTR", var(1000), var(1001)),
                        createInstruction(LABEL, var(1001)),
                        createInstruction(SET, var(0), "__fn0retval"),
                        createInstruction(SET, var(1), "0"),
                        createInstruction(WRITE, var(0), "HEAPPTR", var(1)),
                        createInstruction(END),
                        // Function definition
                        createInstruction(LABEL, var(1000)),
                        createInstruction(SET, var(2), "0"),
                        createInstruction(CALL, "STACKPTR", var(1000), var(1002)),
                        createInstruction(LABEL, var(1002)),
                        createInstruction(SET, var(3), "__fn0retval"),
                        createInstruction(SET, "__fn0retval", var(3)),
                        createInstruction(LABEL, var(1003)),
                        createInstruction(RETURN, "STACKPTR"),
                        createInstruction(END)
                ),
                generateUnoptimized(
                        (Seq) translateToAst(""
                                + "set STACKPTR = cell1 "
                                + "set HEAPPTR = cell2 "
                                + "allocate heap in HEAPPTR[0...16], stack in STACKPTR "
                                + "def delay "
                                + "  0 "
                                + "  delay() "
                                + "end "
                                + "$dx = delay() "
                        )
                )
        );
    }

    @Test
    void handlesNestedFunctionCallsInline() {
        assertLogicInstructionsMatch(
                List.of(
                        createInstruction(SET, var(0), "4"),
                        createInstruction(LABEL, var(1000)),
                        createInstruction(SET, "__fn0_n", var(0)),
                        createInstruction(SET, var(2), "1"),
                        createInstruction(OP, "add", var(3), "__fn0_n", var(2)),
                        createInstruction(SET, var(1), var(3)),
                        createInstruction(LABEL, var(1001)),
                        createInstruction(LABEL, var(1002)),
                        createInstruction(SET, "__fn1_n", var(1)),
                        createInstruction(SET, var(5), "1"),
                        createInstruction(OP, "add", var(6), "__fn1_n", var(5)),
                        createInstruction(SET, var(4), var(6)),
                        createInstruction(LABEL, var(1003)),
                        createInstruction(LABEL, var(1004)),
                        createInstruction(SET, "__fn2_n", var(4)),
                        createInstruction(SET, var(8), "1"),
                        createInstruction(OP, "add", var(9), "__fn2_n", var(8)),
                        createInstruction(SET, var(7), var(9)),
                        createInstruction(LABEL, var(1005)),
                        createInstruction(PRINT, var(7)),
                        createInstruction(END)
                ),
                generateUnoptimized(
                        (Seq) translateToAst(""
                                + "inline def a(n) n + 1 end "
                                + "print(a(a(a(4))))"
                        )
                )
        );
    }

    @Test
    void handlesNestedFunctionCallsStackless() {
        assertLogicInstructionsMatch(
                List.of(
                        createInstruction(SET, var(0), "4"),
                        createInstruction(SET, "__fn0_n", var(0)),
                        createInstruction(SET, "__fn0retaddr", var(1001)),
                        createInstruction(SET, "@counter", var(1000)),
                        createInstruction(LABEL, var(1001)),
                        createInstruction(SET, var(1), "__fn0retval"),
                        createInstruction(SET, "__fn0_n", var(1)),
                        createInstruction(SET, "__fn0retaddr", var(1002)),
                        createInstruction(SET, "@counter", var(1000)),
                        createInstruction(LABEL, var(1002)),
                        createInstruction(SET, var(2), "__fn0retval"),
                        createInstruction(SET, "__fn0_n", var(2)),
                        createInstruction(SET, "__fn0retaddr", var(1003)),
                        createInstruction(SET, "@counter", var(1000)),
                        createInstruction(LABEL, var(1003)),
                        createInstruction(SET, var(3), "__fn0retval"),
                        createInstruction(PRINT, var(3)),
                        createInstruction(END),
                        // def a
                        createInstruction(LABEL, var(1000)),
                        createInstruction(SET, var(4), "1"),
                        createInstruction(OP, "add", var(5), "__fn0_n", var(4)),
                        createInstruction(SET, "__fn0retval", var(5)),
                        createInstruction(LABEL, var(1004)),
                        createInstruction(SET, "@counter", "__fn0retaddr"),
                        createInstruction(END)
                ),
                generateUnoptimized(
                        (Seq) translateToAst(""
                                + "def a(n) n + 1 end "
                                + "print(a(a(a(4))))"
                        )
                )
        );
    }

    @Test
    void handlesNestedFunctionCallsRecursive() {
        assertLogicInstructionsMatch(
                List.of(
                        createInstruction(SET, "__sp", "511"),
                        createInstruction(SET, var(0), "4"),
                        createInstruction(SET, "__fn0_n", var(0)),
                        createInstruction(CALL, "bank1", var(1000), var(1001)),
                        createInstruction(LABEL, var(1001)),
                        createInstruction(SET, var(1), "__fn0retval"),
                        createInstruction(SET, "__fn0_n", var(1)),
                        createInstruction(CALL, "bank1", var(1000), var(1002)),
                        createInstruction(LABEL, var(1002)),
                        createInstruction(SET, var(2), "__fn0retval"),
                        createInstruction(SET, "__fn0_n", var(2)),
                        createInstruction(CALL, "bank1", var(1000), var(1003)),
                        createInstruction(LABEL, var(1003)),
                        createInstruction(SET, var(3), "__fn0retval"),
                        createInstruction(PRINT, var(3)),
                        createInstruction(END),
                        // def a
                        createInstruction(LABEL, var(1000)),
                        createInstruction(SET, var(4), "1"),
                        createInstruction(OP, "add", var(5), "__fn0_n", var(4)),
                        // call a
                        createInstruction(PUSH, "bank1", "__fn0_n"),
                        createInstruction(SET, "__fn0_n", var(5)),
                        createInstruction(CALL, "bank1", var(1000), var(1004)),
                        createInstruction(LABEL, var(1004)),
                        createInstruction(POP, "bank1", "__fn0_n"),
                        createInstruction(SET, var(6), "__fn0retval"),
                        createInstruction(SET, "__fn0retval", var(6)),
                        createInstruction(LABEL, var(1005)),
                        createInstruction(RETURN, "bank1"),
                        createInstruction(END)
                ),
                generateUnoptimized(
                        (Seq) translateToAst(""
                                + "allocate stack in bank1[0...512] "
                                + "def a(n) a(n + 1) end "
                                + "print(a(a(a(4))))"
                        )
                )
        );
    }

    @Test
    void generatesRecursiveFibonacci() {
        assertLogicInstructionsMatch(
                List.of(
                        createInstruction(SET, "__sp", "511"),
                        createInstruction(SET, var(0), "10"),
                        createInstruction(SET, "__fn0_n", var(0)),
                        createInstruction(CALL, "bank1", var(1000), var(1001)),
                        createInstruction(LABEL, var(1001)),
                        createInstruction(SET, var(1), "__fn0retval"),
                        createInstruction(PRINT, var(1)),
                        createInstruction(END),
                        // def fib
                        createInstruction(LABEL, var(1000)),
                        createInstruction(SET, var(2), "2"),
                        createInstruction(OP, "lessThan", var(3), "__fn0_n", var(2)),
                        createInstruction(JUMP, var(1002), "equal", var(3), "false"),
                        createInstruction(SET, var(4), "__fn0_n"),
                        createInstruction(JUMP, var(1003), "always"),
                        createInstruction(LABEL, var(1002)),
                        createInstruction(SET, var(5), "1"),
                        createInstruction(OP, "sub", var(6), "__fn0_n", var(5)),
                        createInstruction(PUSH, "bank1", "__fn0_n"),
                        createInstruction(SET, "__fn0_n", var(6)),
                        createInstruction(CALL, "bank1", var(1000), var(1004)),
                        createInstruction(LABEL, var(1004)),
                        createInstruction(POP, "bank1", "__fn0_n"),
                        createInstruction(SET, var(7), "__fn0retval"),
                        createInstruction(SET, var(8), "2"),
                        createInstruction(OP, "sub", var(9), "__fn0_n", var(8)),
                        createInstruction(PUSH, "bank1", "__fn0_n"),
                        createInstruction(PUSH, "bank1", var(7)),
                        createInstruction(SET, "__fn0_n", var(9)),
                        createInstruction(CALL, "bank1", var(1000), var(1005)),
                        createInstruction(LABEL, var(1005)),
                        createInstruction(POP, "bank1", var(7)),
                        createInstruction(POP, "bank1", "__fn0_n"),
                        createInstruction(SET, var(10), "__fn0retval"),
                        createInstruction(OP, "add", var(11), var(7), var(10)),
                        createInstruction(SET, var(4), var(11)),
                        createInstruction(LABEL, var(1003)),
                        createInstruction(SET, "__fn0retval", var(4)),
                        createInstruction(LABEL, var(1006)),
                        createInstruction(RETURN, "bank1"),
                        createInstruction(END)
                ),
                generateUnoptimized(
                        (Seq) translateToAst(""
                                + "allocate stack in bank1[0...512] "
                                + "def fib(n) "
                                + "    n < 2 ? n : fib(n - 1) + fib(n - 2) "
                                + "end "
                                + "print(fib(10))"
                        )
                )
        );
    }

    @Test
    void doesNotPushInlineFunctionVariables() {
        // Verifies that locals inside inline functions (which by definition are limited to the function scope)
        // aren't pushed to the stack in recursive functions
        assertLogicInstructionsMatch(
                List.of(
                        createInstruction(SET, "__sp", "511"),
                        createInstruction(SET, var(0), "1"),
                        createInstruction(SET, "__fn0_n", var(0)),
                        createInstruction(CALL, "bank1", var(1000), var(1001)),
                        createInstruction(LABEL, var(1001)),
                        createInstruction(SET, var(1), "__fn0retval"),
                        createInstruction(END),
                        // def foo
                        createInstruction(LABEL, var(1000)),
                        createInstruction(LABEL, var(1013)),
                        // call bar (inline - creates __fn1_n)
                        createInstruction(SET, "__fn1_n", "__fn0_n"),
                        createInstruction(PRINT, "__fn1_n"),
                        createInstruction(SET, var(9), "__fn1_n"),
                        createInstruction(LABEL, var(1004)),
                        createInstruction(SET, var(2), "1"),
                        createInstruction(OP, "sub", var(3), "__fn0_n", var(2)),
                        createInstruction(PUSH, "bank1", "__fn0_n"),
                        createInstruction(SET, "__fn0_n", var(3)),
                        createInstruction(CALL, "bank1", var(1000), var(1002)),
                        createInstruction(LABEL, var(1002)),
                        createInstruction(POP, "bank1", "__fn0_n"),
                        createInstruction(SET, var(4), "__fn0retval"),
                        createInstruction(SET, "__fn0retval", var(4)),
                        createInstruction(LABEL, var(1003)),
                        createInstruction(RETURN, "bank1"),
                        createInstruction(END)
                ),
                generateUnoptimized(
                        (Seq) translateToAst(""
                                + "allocate stack in bank1[0...512] "
                                + "def foo(n) "
                                + "    bar(n) "
                                + "    foo(n - 1) "
                                + "end "
                                + "inline def bar(n) "
                                + "    print(n) "
                                + "end "
                                + "foo(1)"
                        )
                )
        );
    }

    @Test
    void preservesBlocksAndConstantsInUserFunctions() {
        assertLogicInstructionsMatch(
                List.of(
                        createInstruction(LABEL, var(1001)),
                        createInstruction(SET, "__fn0_block", "lancer1"),
                        createInstruction(SET, var(1), "1"),
                        createInstruction(RADAR, "enemy", "any", "any", "distance", "__fn0_block", var(1), var(2)),
                        createInstruction(PRINT, var(2)),
                        createInstruction(SET, var(3), "1"),
                        createInstruction(RADAR, "ally", "flying", "any", "health", "__fn0_block", var(3), var(4)),
                        createInstruction(PRINT, var(4)),
                        createInstruction(SET, var(5), "1"),
                        createInstruction(RADAR, "enemy", "boss", "any", "distance", "lancer1", var(5), var(6)),
                        createInstruction(PRINT, var(6)),
                        createInstruction(SET, var(0), var(6)),
                        createInstruction(LABEL, var(1000)),
                        createInstruction(END)
                ),
                generateUnoptimized(
                        (Seq) translateToAst(""
                                + "def foo(block) "
                                + "  print(radar(enemy, any, any, distance, block, 1)) "
                                + "  print(block.radar(ally, flying, any, health, 1)) "
                                + "  print(radar(enemy, boss, any, distance, lancer1, 1)) "
                                + "end "
                                + "foo(lancer1)"
                        )
                )
        );
    }

    @Test
    void handlesInlineReturn() {
        assertLogicInstructionsMatch(
                List.of(
                        createInstruction(SET, var(0), "0"),
                        createInstruction(LABEL, var(1010)),
                        createInstruction(SET, "__fn0_n", var(0)),
                        createInstruction(SET, var(2), "2"),
                        createInstruction(OP, "mod", var(3), "__fn0_n", var(2)),
                        createInstruction(SET, var(4), "1"),
                        createInstruction(OP, "equal", var(5), var(3), var(4)),
                        createInstruction(JUMP, var(1001), "equal", var(5), "false"),
                        createInstruction(SET, var(7), "\"odd\""),
                        createInstruction(SET, var(1), var(7)),
                        createInstruction(JUMP, var(1000), "always"),
                        createInstruction(SET, var(6), "null"),
                        createInstruction(JUMP, var(1002), "always"),
                        createInstruction(LABEL, var(1001)),
                        createInstruction(SET, var(8), "\"even\""),
                        createInstruction(SET, var(1), var(8)),
                        createInstruction(JUMP, var(1000), "always"),
                        createInstruction(SET, var(6), "null"),
                        createInstruction(LABEL, var(1002)),
                        createInstruction(SET, var(1), var(6)),
                        createInstruction(LABEL, var(1000)),
                        createInstruction(PRINT, var(1)),
                        createInstruction(END)
                ),
                generateUnoptimized(
                        (Seq) translateToAst(""
                                + "def a(n) "
                                + "    if n % 2 == 1 "
                                + "        return \"odd\" "
                                + "    else "
                                + "        return \"even\" "
                                + "    end "
                                + "end "
                                + "print(a(0))"
                        )
                )
        );
    }

    @Test
    void handlesStacklessReturn() {
        assertLogicInstructionsMatch(
                List.of(
                        createInstruction(SET, var(0), "0"),
                        createInstruction(SET, "__fn0_n", var(0)),
                        createInstruction(SET, "__fn0retaddr", var(1001)),
                        createInstruction(SET, "@counter", var(1000)),
                        createInstruction(LABEL, var(1001)),
                        createInstruction(SET, var(1), "__fn0retval"),
                        createInstruction(PRINT, var(1)),
                        createInstruction(SET, var(2), "1"),
                        createInstruction(SET, "__fn0_n", var(2)),
                        createInstruction(SET, "__fn0retaddr", var(1002)),
                        createInstruction(SET, "@counter", var(1000)),
                        createInstruction(LABEL, var(1002)),
                        createInstruction(SET, var(3), "__fn0retval"),
                        createInstruction(PRINT, var(3)),
                        createInstruction(END),
                        // def a
                        createInstruction(LABEL, var(1000)),
                        createInstruction(SET, var(4), "2"),
                        createInstruction(OP, "mod", var(5), "__fn0_n", var(4)),
                        createInstruction(SET, var(6), "1"),
                        createInstruction(OP, "equal", var(7), var(5), var(6)),
                        createInstruction(JUMP, var(1004), "equal", var(7), "false"),
                        createInstruction(SET, var(9), "\"odd\""),
                        createInstruction(SET, "__fn0retval", var(9)),
                        createInstruction(JUMP, var(1003), "always"),
                        createInstruction(SET, var(8), "null"),
                        createInstruction(JUMP, var(1005), "always"),
                        createInstruction(LABEL, var(1004)),
                        createInstruction(SET, var(10), "\"even\""),
                        createInstruction(SET, "__fn0retval", var(10)),
                        createInstruction(JUMP, var(1003), "always"),
                        createInstruction(SET, var(8), "null"),
                        createInstruction(LABEL, var(1005)),
                        createInstruction(SET, "__fn0retval", var(8)),
                        createInstruction(LABEL, var(1003)),
                        createInstruction(SET, "@counter", "__fn0retaddr"),
                        createInstruction(END)
                ),
                generateUnoptimized(
                        (Seq) translateToAst(""
                                + "def a(n) "
                                + "    if n % 2 == 1 "
                                + "        return \"odd\" "
                                + "    else "
                                + "        return \"even\" "
                                + "    end "
                                + "end "
                                + "print(a(0))"
                                + "print(a(1))"
                        )
                )
        );
    }

    @Test
    void handlesRecursiveReturn() {
        assertLogicInstructionsMatch(
                List.of(
                        createInstruction(SET, "__sp", "511"),
                        // call gdc
                        createInstruction(SET, var(0), "115"),
                        createInstruction(SET, var(1), "78"),
                        createInstruction(SET, "__fn0_a", var(0)),
                        createInstruction(SET, "__fn0_b", var(1)),
                        createInstruction(CALL, "bank1", var(1000), var(1001)),
                        createInstruction(LABEL, var(1001)),
                        createInstruction(SET, var(2), "__fn0retval"),
                        createInstruction(PRINT, var(2)),
                        createInstruction(END),
                        // def gdc
                        createInstruction(LABEL, var(1000)),
                        createInstruction(SET, var(3), "0"),
                        createInstruction(OP, "equal", var(4), "__fn0_b", var(3)),
                        createInstruction(JUMP, var(1003), "equal", var(4), "false"),
                        // return a
                        createInstruction(SET, "__fn0retval", "__fn0_a"),
                        createInstruction(JUMP, var(1002), "always"),
                        createInstruction(SET, var(5), "null"),
                        createInstruction(JUMP, var(1004), "always"),
                        createInstruction(LABEL, var(1003)),
                        // call gdc
                        createInstruction(OP, "mod", var(6), "__fn0_a", "__fn0_b"),
                        createInstruction(PUSH, "bank1", "__fn0_a"),
                        createInstruction(PUSH, "bank1", "__fn0_b"),
                        createInstruction(SET, "__fn0_a", "__fn0_b"),
                        createInstruction(SET, "__fn0_b", var(6)),
                        createInstruction(CALL, "bank1", var(1000), var(1005)),
                        createInstruction(LABEL, var(1005)),
                        createInstruction(POP, "bank1", "__fn0_b"),
                        createInstruction(POP, "bank1", "__fn0_a"),
                        createInstruction(SET, var(7), "__fn0retval"),
                        // return gdc(...)
                        createInstruction(SET, "__fn0retval", var(7)),
                        createInstruction(JUMP, var(1002), "always"),
                        createInstruction(SET, var(5), "null"),
                        createInstruction(LABEL, var(1004)),
                        createInstruction(SET, "__fn0retval", var(5)),
                        createInstruction(LABEL, var(1002)),
                        createInstruction(RETURN, "bank1"),
                        createInstruction(END)
                ),
                generateUnoptimized(
                        (Seq) translateToAst(""
                                + "allocate stack in bank1[0...512] "
                                + "def gdc(a,b) "
                                + "    if b == 0 "
                                + "        return a "
                                + "    else "
                                + "        return gdc(b, a % b) "
                                + "    end "
                                + "end "
                                + "print(gdc(115, 78))"
                        )
                )
        );
    }

    @Test
    void handlesNestedInlineReturn() {
        assertLogicInstructionsMatch(
                List.of(
                        createInstruction(SET, var(0), "4"),
                        createInstruction(LABEL, var(1010)),
                        createInstruction(SET, "__fn0_n", var(0)),
                        createInstruction(SET, var(2), "1"),
                        createInstruction(LABEL, var(1012)),
                        createInstruction(SET, "__fn1_n", "__fn0_n"),
                        createInstruction(SET, var(4), "2"),
                        createInstruction(LABEL, var(1014)),
                        createInstruction(SET, "__fn2_n", "__fn1_n"),
                        createInstruction(SET, var(6), "3"),
                        createInstruction(OP, "pow", var(7), var(6), "__fn2_n"),
                        createInstruction(SET, var(5), var(7)),
                        createInstruction(JUMP, var(1002), "always"),
                        createInstruction(SET, var(5), "null"),
                        createInstruction(LABEL, var(1002)),
                        createInstruction(OP, "mul", var(8), var(4), var(5)),
                        createInstruction(SET, var(3), var(8)),
                        createInstruction(JUMP, var(1001), "always"),
                        createInstruction(SET, var(3), "null"),
                        createInstruction(LABEL, var(1001)),
                        createInstruction(OP, "add", var(9), var(2), var(3)),
                        createInstruction(SET, var(1), var(9)),
                        createInstruction(JUMP, var(1000), "always"),
                        createInstruction(SET, var(1), "null"),
                        createInstruction(LABEL, var(1000)),
                        createInstruction(PRINT, var(1)),
                        createInstruction(END)
                ),
                generateUnoptimized(
                        (Seq) translateToAst(""
                                + "def foo(n) return 1 + bar(n) end "
                                + "def bar(n) return 2 * baz(n) end "
                                + "def baz(n) return 3 ** n end "
                                + "print(foo(4))"
                        )
                )
        );
    }

    @Test
    void handlesNestedFunctionCallsInlineWithReturn() {
        assertLogicInstructionsMatch(
                List.of(
                        createInstruction(SET, var(0), "4"),
                        createInstruction(LABEL, var(1000)),
                        createInstruction(SET, "__fn0_n", var(0)),
                        createInstruction(SET, var(2), "1"),
                        createInstruction(OP, "add", var(3), "__fn0_n", var(2)),
                        createInstruction(SET, var(1), var(3)),
                        createInstruction(JUMP, var(1001), "always"),
                        createInstruction(SET, var(1), "null"),
                        createInstruction(LABEL, var(1001)),
                        createInstruction(LABEL, var(1002)),
                        createInstruction(SET, "__fn1_n", var(1)),
                        createInstruction(SET, var(5), "1"),
                        createInstruction(OP, "add", var(6), "__fn1_n", var(5)),
                        createInstruction(SET, var(4), var(6)),
                        createInstruction(JUMP, var(1003), "always"),
                        createInstruction(SET, var(4), "null"),
                        createInstruction(LABEL, var(1003)),
                        createInstruction(LABEL, var(1004)),
                        createInstruction(SET, "__fn2_n", var(4)),
                        createInstruction(SET, var(8), "1"),
                        createInstruction(OP, "add", var(9), "__fn2_n", var(8)),
                        createInstruction(SET, var(7), var(9)),
                        createInstruction(JUMP, var(1005), "always"),
                        createInstruction(SET, var(7), "null"),
                        createInstruction(LABEL, var(1005)),
                        createInstruction(PRINT, var(7)),
                        createInstruction(END)
                ),
                generateUnoptimized(
                        (Seq) translateToAst(""
                                + "inline def a(n) return n + 1 end "
                                + "print(a(a(a(4))))"
                        )
                )
        );
    }

    @Test
    void accessesMainVariables() {
        assertLogicInstructionsMatch(
                List.of(
                        createInstruction(SET, var(0), "7"),
                        createInstruction(LABEL, var(1010)),
                        createInstruction(SET, "__fn0_x", var(0)),
                        createInstruction(SET, "X", "__fn0_x"),
                        createInstruction(SET, var(1), "__fn0_x"),
                        createInstruction(LABEL, var(1000)),
                        createInstruction(PRINT, "X"),
                        createInstruction(END)
                ),
                generateUnoptimized(
                        (Seq) translateToAst(""
                                + "def setx(x) "
                                + "    X = x "
                                + "end "
                                + "setx(7) "
                                + "print(X)"
                        )
                )
        );
    }

    @Test
    void allocateStackInNonStandardWayProducesCorrectCode() {
        // in this test, we're only concerned with whether or not the top of the stack is respected, and whether or
        // not the start of heap is respected. Everything else superfluous.
        assertLogicInstructionsMatch(
                List.of(
                        createInstruction(SET, "__sp", "40"),
                        createInstruction(SET, var(0), "99"),
                        createInstruction(SET, var(1), "41"),
                        createInstruction(WRITE, var(0), "cell3", var(1))
                ),
                generateUnoptimized(
                        (Seq) translateToAst(""
                                + "allocate stack in cell3[0..40], heap in cell3[41...64] "
                                + "def foo(n) "
                                + "  foo(n-1) "
                                + "end "
                                + " "
                                + "$x = 99 "
                                + "print(foo(1) + foo(2))"
                        )
                ).subList(0, 4)
        );
    }

    @Test
    void refusesToDeclareRecursiveFunctionsWhenNoStackAround() {
        assertThrows(MissingStackException.class,
                () -> generateUnoptimized(
                        (Seq) translateToAst(""
                                + "def foo "
                                + "  foo() "
                                + "end "
                                + "foo()"
                        )
                )
        );
    }

    @Test
    void refusesMisplacedStackAllocation() {
        assertThrows(MisplacedStackAllocationException.class,
                () -> generateUnoptimized(
                        (Seq) translateToAst(""
                                + "if true "
                                + "  allocate stack in cell1 "
                                + "end"
                        )
                )
        );
    }

    @Test
    void refusesRecursiveInlineFunctions() {
        assertThrows(InlineRecursiveFunctionException.class,
                () -> generateUnoptimized(
                        (Seq) translateToAst(""
                                + "allocate stack in cell1 "
                                + "inline def foo(n) "
                                + "  foo(n-1) "
                                + "end "
                                + "print(foo(1) + foo(2))"
                        )
                )
        );
    }

    @Test
    void refusesUppercaseFunctionParameter() {
        assertThrows(InvalidParameterNameException.class,
                () -> generateUnoptimized(
                        (Seq) translateToAst(""
                                + "def foo(N) "
                                + "  N "
                                + "end "
                                + "print(foo(1) + foo(2))"
                        )
                )
        );
    }

    @Test
    void refusesBlockNameAsFunctionParameter() {
        assertThrows(InvalidParameterNameException.class,
                () -> generateUnoptimized(
                        (Seq) translateToAst(""
                                + "def foo(switch1) "
                                + "  switch1 "
                                + "end "
                                + "print(foo(1) + foo(2))"
                        )
                )
        );
    }
}
