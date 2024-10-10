package info.teksol.mindcode.grammar;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MindcodeParserTest extends AbstractParserTest {

    @Test
    void parsesDecimalNumber() {
        assertDoesNotThrow(() -> parse("0123456789;"));
    }

    @Test
    void parsesHexadecimalNumber() {
        assertDoesNotThrow(() -> parse("0x0123456789ABCDEFabcdef;"));
    }

    @Test
    void parsesBinaryNumber() {
        assertDoesNotThrow(() -> parse("0b010101;"));
    }

    @Test
    void parsesFloatingPointNumber() {
        assertDoesNotThrow(() -> parse("1.0;"));
        assertDoesNotThrow(() -> parse("0.0;"));
        assertDoesNotThrow(() -> parse("1e5;"));
        assertDoesNotThrow(() -> parse("1e+5;"));
        assertDoesNotThrow(() -> parse("1e-5;"));
        assertDoesNotThrow(() -> parse("1E10;"));
        assertDoesNotThrow(() -> parse("1E+10;"));
        assertDoesNotThrow(() -> parse("1E-10;"));
        assertDoesNotThrow(() -> parse("2.5E10;"));
        assertDoesNotThrow(() -> parse("2.5E+10;"));
        assertDoesNotThrow(() -> parse("2.5E-10;"));
    }

    @Test
    void parsesTheEmptyProgram() {
        assertDoesNotThrow(() -> parse(""));
    }

    @Test
    void parsesSensorAccess() {
        assertDoesNotThrow(() -> parse("foundation1.copper < 1000;"));
        assertDoesNotThrow(() -> parse("foundation1.copper < foundation1.itemCapacity;"));
        assertDoesNotThrow(() -> parse("tank1.water < tank1.liquidCapacity;"));
    }

    @Test
    void parsesControlStatements() {
        assertDoesNotThrow(() -> parse("conveyor1.enabled = foundation1.copper < foundation1.itemCapacity;"));
    }

    @Test
    void parsesSimpleRvalues() {
        assertDoesNotThrow(() -> parse("foo;\nbar;\n"));
    }

    @Test
    void parsesAssignmentOfCalculations() {
        assertDoesNotThrow(() -> parse("foo = bar ** (n - 2);"));
    }

    @Test
    void parsesSimpleWhileLoop() {
        assertDoesNotThrow(() -> parse("n = 5;\nwhile n > 0 do\nn -= 1;\nend;"));
    }

    @Test
    void reportsSyntaxError() {
        List<String> errors = parseWithErrors("while");
        assertEquals(1, errors.size(), "Expected one syntax error");
    }

    @Test
    void parsesHeapAccesses() {
        assertDoesNotThrow(() -> parse("cell1[0] = cell[1] + 1;"));
    }

    @Test
    void parsesIfExpression() {
        assertDoesNotThrow(() -> parse("value = HEAP[4] == 0 ? false : true;"));
        assertDoesNotThrow(() -> parse("if false then\nn += 1;\nend;"));
    }

    @Test
    void parsesRefs() {
        assertDoesNotThrow(() -> parse("while @unit == null do\nubind(poly);\nend;\n"));
    }

    @Test
    void parsesFlagAssignment() {
        assertDoesNotThrow(() -> parse("flag(FLAG);"));
    }

    @Test
    void parsesUnaryMinus() {
        assertDoesNotThrow(() -> parse("-1 * dx;"));
    }

    @Test
    void parsesHeapReferencesWithRvalues() {
        assertDoesNotThrow(() -> parse("cell1[dx] = 1;"));
    }

    @Test
    void parsesCStyleForLoop() {
        assertDoesNotThrow(() -> parse("for i = 0, j = 0; i < j ; i += 1 do\nprint(j);\nend;"));
    }

    @Test
    void parsesInclusiveIteratorStyleLoop() {
        assertDoesNotThrow(() -> parse("for n in 1 .. 17 do\nprint(n);\nend;\n"));
    }

    @Test
    void parsesExclusiveIteratorStyleLoop() {
        assertDoesNotThrow(() -> parse("for n in 1 ... 17 do\nprint(n);\nend;\n"));
    }

    @Test
    void parsesHeapAllocation() {
        assertDoesNotThrow(() -> parse("allocate heap in cell4[0 .. 64];"));
    }

    @Test
    void parsesGlobalReferences() {
        assertDoesNotThrow(() -> parse("""
                        allocate heap in cell2[14 .. 20];
                        $dx += 1;
                        $dy = $dx - 4;
                        """
                )
        );
    }

    @Test
    void parsesRemarkComments() {
        assertDoesNotThrow(() -> parse("""
                        // This is a comment
                        /// This is a remark
                        """
                )
        );
    }

    @Test
    void refusesInvalidInputs() {
        assertEquals("""
                        Syntax error: [@0,0:0='1',<89>,1:0] on line 1:0: missing ';'
                        Syntax error: [@1,1:1='.',<38>,1:1] on line 1:1: mismatched input '.' expecting {<EOF>, 'allocate', 'break', 'case', 'const', 'continue', 'def', 'do', 'end', 'false', 'for', 'if', 'inline', 'noinline', 'null', 'param', 'return', 'true', 'while', '@', '/', '\\', '$', '**', '-', '%', '*', NOT, '~', '+', '?', '#set', '#strict', '#relaxed', '<', '<=', '!=', '==', '===', '!==', '>=', '>', AND, OR, '<<', '>>', '&', '|', '^', '(', FORMATTABLE, LITERAL, FLOAT, INT, HEXINT, BININT, ID, REM_COMMENT}""",
                String.join("\n", parseWithErrors("1..0;")));

        assertEquals(
                "Syntax error: [@0,0:0='8',<89>,1:0] on line 1:0: missing ';'",
                String.join("\n", parseWithErrors("8A;")));

        assertEquals(
                "Syntax error: [@0,0:5='fluffy',<92>,1:0] on line 1:0: missing ';'",
                String.join("\n", parseWithErrors("fluffy!bunny;")));
    }
}
