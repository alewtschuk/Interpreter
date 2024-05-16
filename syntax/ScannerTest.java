package syntax;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Scanner
 *
 * Uses Junit5.
 */
public class ScannerTest {

    /**
     * Simply creates a 'program' that has only one token. When scanned,
     * the test checks to see that the current token is of the correct type.
     *
     * Try more than one Token in a different test case!
     * @throws SyntaxException - This suppresses the need for a try/catch block.
     */
    @Test
    public void test() throws SyntaxException{

        String prg = "4";
        Scanner scanner = new Scanner(prg);
        assertTrue(scanner.next());
        assertEquals(new Token("num", "4"), scanner.getCurrent());

        assertFalse(scanner.next());
    }


    /**
     * Tests that the scanner can recognize an identifier
     *
     * @throws SyntaxException
     */
    @Test
    public void testOneIdentifier() throws SyntaxException{

        String prg = "x";
        Scanner scanner = new Scanner(prg);

        assertTrue(scanner.next());
        assertEquals(new Token("id", "x"), scanner.getCurrent());

        assertFalse(scanner.next());
    }


    /**
     * Tests that the scanner can recognize an operator (the semicolon)
     * @throws SyntaxException
     */
    @Test
    public void testOneOperator() throws SyntaxException{

        String prg = ";";
        Scanner scanner = new Scanner(prg);

        assertTrue(scanner.next());
        assertEquals(new Token(";", ";"), scanner.getCurrent());

        assertFalse(scanner.next());
    }

    /**
     * Tests decimal before num
     * @throws SyntaxException - This suppresses the need for a try/catch block.
     */
    @Test
    public void testDBeforeNum() throws SyntaxException{

        String prg = ".4";
        Scanner scanner = new Scanner(prg);
        assertTrue(scanner.next());
        assertEquals(new Token("num", ".4"), scanner.getCurrent());

        assertFalse(scanner.next());
    }

    /**
     * Tests decimal before num
     * @throws SyntaxException - This suppresses the need for a try/catch block.
     */
    @Test
    public void testMiddleDecimal() throws SyntaxException{

        String prg = "4.4";
        Scanner scanner = new Scanner(prg);
        assertTrue(scanner.next());
        assertEquals(new Token("num", "4.4"), scanner.getCurrent());

        assertFalse(scanner.next());
    }

    /**
     * Test for multiple tokens 
     * @throws SyntaxException
     */
    @Test
    public void testMultipleTokens() throws SyntaxException {
        String prg = "var x = 5;";
        Scanner scanner = new Scanner(prg);

        assertTrue(scanner.next());
        assertEquals(new Token("id", "var"), scanner.getCurrent());

        assertTrue(scanner.next());
        assertEquals(new Token("id", "x"), scanner.getCurrent());

        assertTrue(scanner.next());
        assertEquals(new Token("=", "="), scanner.getCurrent());

        assertTrue(scanner.next());
        assertEquals(new Token("num", "5"), scanner.getCurrent());

        assertTrue(scanner.next());
        assertEquals(new Token(";", ";"), scanner.getCurrent());

        assertFalse(scanner.next());
    }

    /**
     * Tests for new line in a program
     * @throws SyntaxException
     */
    @Test
    public void testNewlinesInProgram() throws SyntaxException {
        String prg = "var\nx;";
        Scanner scanner = new Scanner(prg);

        assertTrue(scanner.next());
        assertEquals(new Token("id", "var"), scanner.getCurrent());

        assertTrue(scanner.next());
        assertEquals(new Token("id", "x"), scanner.getCurrent());

        assertTrue(scanner.next());
        assertEquals(new Token(";", ";"), scanner.getCurrent());

        assertFalse(scanner.next());
    }

    /**
     * Tests operators
     * @throws SyntaxException
     */
    @Test
    public void testOtherOperators() throws SyntaxException {
        String prg = "+-*/()=";
        Scanner scanner = new Scanner(prg);

        String[] operators = {"+", "-", "*", "/", "(", ")", "="};
        for (String op : operators) {
            assertTrue(scanner.next());
            assertEquals(new Token(op, op), scanner.getCurrent());
        }

        assertFalse(scanner.next());
    }

    /**
     * Tests for commens beginning and ending with !!
     * @throws SyntaxException
     */
    @Test
    public void testComments() throws SyntaxException {
        String prg = "!! This is a comment !!\nvar x = 5;";
        Scanner scanner = new Scanner(prg);

        // Skip the comment and check the tokens after it
        assertTrue(scanner.next());
        assertEquals(new Token("id", "var"), scanner.getCurrent());

        assertTrue(scanner.next());
        assertEquals(new Token("id", "x"), scanner.getCurrent());

        assertTrue(scanner.next());
        assertEquals(new Token("=", "="), scanner.getCurrent());

        assertTrue(scanner.next());
        assertEquals(new Token("num", "5"), scanner.getCurrent());

        assertTrue(scanner.next());
        assertEquals(new Token(";", ";"), scanner.getCurrent());

        assertFalse(scanner.next());
    }

    /**
     * Tests for multiple decimals in a number
     * @throws SyntaxException
     */
    @Test
    public void testMultipleDecimalsInNumber() throws SyntaxException {
        String prg = "12.34.56";
        Scanner scanner = new Scanner(prg);
        scanner.next(); // This should trigger SyntaxException and test should pass
    }

    /**
     * Tests multi-line comment handling
     * @throws SyntaxException
     */
    @Test
    public void testMultiLineComment() throws SyntaxException {
        String prg = "!! This is a multi-line \n comment  with new lines !!";
        Scanner scanner = new Scanner(prg);

        assertFalse(scanner.next()); // Comment should not be considered a token
    }

    /**
     * Slightly harder testing of multi-line comment handling
     * @throws SyntaxException
     */
    @Test
    public void testHarderMultiLineComment() throws SyntaxException {
        String prg = "!!This is a multi-line \n comment  with new lines !! But now there are words after";
        Scanner scanner = new Scanner(prg);

        assertTrue(scanner.next()); // Find multiple tokens after comment
        assertEquals(new Token("id", "But"), scanner.getCurrent());
        assertTrue(scanner.next()); // Find the comment end
        assertEquals(new Token("id", "now"), scanner.getCurrent());
        assertTrue(scanner.next()); // Find the next token after the comment
        assertEquals(new Token("id", "there"), scanner.getCurrent());
    }

    /**
     * Hardest testing of multi-line comment handling to make sure parses before and after comments
     * properly
     * @throws SyntaxException
     */
    @Test
    public void testHardestMultiLineComment() throws SyntaxException {
        String prg = "77 !! This is a multi-line \n comment  with new lines !! 11. 122 .1 .";
        Scanner scanner = new Scanner(prg);

        assertTrue(scanner.next()); // make sure there is a token found next
        assertEquals(new Token("num", "77"), scanner.getCurrent());//get first number
        assertTrue(scanner.next()); // Moves over comment as comments are ignored then tests getting the numbers
        assertEquals(new Token("num", "11."), scanner.getCurrent()); // Parsing Numbers correctly after
        assertTrue(scanner.next()); // Find the next token
        assertEquals(new Token("num", "122"), scanner.getCurrent());
        assertTrue(scanner.next()); // Find the next token
        assertEquals(new Token("num", ".1"), scanner.getCurrent());
        assertTrue(scanner.next()); // Find the next token
        assertEquals(new Token("num", "."), scanner.getCurrent());

        assertFalse(scanner.next()); // Nothing else to see

    }

    /**
     * Tests handling of negative numbers as seperate tokens
     * @throws SyntaxException
     */
    @Test
    public void testNegativeNumber() throws SyntaxException {
        String prg = "-10";
        Scanner scanner = new Scanner(prg);

        assertTrue(scanner.next());// Finds token
        assertEquals(new Token("-", "-"), scanner.getCurrent());
        assertTrue(scanner.next());
        assertEquals(new Token("num", "10"), scanner.getCurrent()); 

        assertFalse(scanner.next()); // Nothing else to see
    }

    /**
     * Tests that the scanner can recognize "." as a number
     *
     * @throws SyntaxException
     */
    @Test
    public void testWeirdNumberRule() throws SyntaxException{

        String prg = ".";
        Scanner scanner = new Scanner(prg);

        assertTrue(scanner.next());
        assertEquals(new Token("num", "."), scanner.getCurrent());

        assertFalse(scanner.next());
    }


}
