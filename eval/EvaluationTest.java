package eval;

import node.Node;
import syntax.Parser;
import syntax.SyntaxException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

class EvaluationTest {

    private Parser parser;
    private Environment env;


    @BeforeEach
    void setUp() {

        parser = new Parser();
        env = new Environment();
    }

    @Test
    void testAssignment() throws SyntaxException, EvalException{
        String prg = "x = 1; wr x";

        double evaluation = parser.parse(prg).eval(env);
        assertEquals(1, evaluation);
    }

    @Test
    void testEvalError() throws SyntaxException {

        String prg = "wr x";
        Node parseTree = parser.parse(prg);

        assertThrows(EvalException.class, () -> {
            parseTree.eval(env);
        });
    }

    @Test
    void testAssociation() throws SyntaxException, EvalException{
        String prg = "wr 10 - 4 - 3";

        double evaluation = parser.parse(prg).eval(env);
        assertEquals(3, evaluation);
    }

    @Test
    void testPrecedence() throws SyntaxException, EvalException{
        String prg = "wr 6 / (10 - 8)";

        double evaluation = parser.parse(prg).eval(env);
        assertEquals(3, evaluation);
    }

    @Test
    void testPrecedence2() throws SyntaxException, EvalException{
        String prg = "wr 6 + 4 / (10 - 8)";

        double evaluation = parser.parse(prg).eval(env);
        assertEquals(8, evaluation);
    }

    @Test
    void testLongAssociation() throws SyntaxException, EvalException{
        String prg = "wr 10 - 4 - 3 - 3 + 10";

        double evaluation = parser.parse(prg).eval(env);
        assertEquals(10, evaluation);
    }


@Test
void testComplexExpression() throws SyntaxException, EvalException {
    parser.parse("x = 10").eval(env);
    parser.parse("y = x - 5").eval(env);
    parser.parse("z = y * 2").eval(env);
    double evaluation = parser.parse("wr z / 2 + 3").eval(env);
    assertEquals(8, evaluation);
}


@Test
void testUndefinedVariable() throws SyntaxException {
    String prg = "wr y";
    Node parseTree = parser.parse(prg);
    assertThrows(EvalException.class, () -> {
        parseTree.eval(env);
    });
}

@Test
void testMultipleOperations() throws SyntaxException, EvalException {
    String prg = "wr 5 + 2 * 3 - 4 / 2";
    double evaluation = parser.parse(prg).eval(env);
    assertEquals(9, evaluation);
}

@Test
void testParentheses() throws SyntaxException, EvalException {
    String prg = "wr (5 + 2) * (3 - 1)";
    double evaluation = parser.parse(prg).eval(env);
    assertEquals(14, evaluation);
}

@Test
void testAssociation3() throws SyntaxException, EvalException{
    String prg = "wr 10 / 2 / 3";

    double evaluation = parser.parse(prg).eval(env);
    assertEquals(1.6666666666666667, evaluation);
}

//NOTE: The below tests I had to have run in a workaround. For some reason they failed in the tester
//but they worked when I ran them in the

@Test
void testGCD() throws SyntaxException, EvalException{
    String inp = "36\n12\n";
    InputStream is = new ByteArrayInputStream(inp.getBytes());
    System.setIn(is);

    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outContent));

    String prg = "rd a; rd b; while a<>b do if a > b then a = a-b else b = b-a; wr a";

    parser.parse(prg).eval(env);

    // Restore original System.out
    System.setOut(originalOut);

    // Convert the captured output to a string and trim to remove trailing newlines
    String output = outContent.toString().trim();

    // Assert that the captured output is the expected Fibonacci number
    assertEquals("12.0", output);
}

@Test
void testFibonacci() throws SyntaxException, EvalException {
    String inp = "10\n";
    InputStream is = new ByteArrayInputStream(inp.getBytes());
    System.setIn(is);

    // Redirect System.out to capture the output
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outContent));

    // Fibonacci program
    String prg = "rd n; if n <= 2 then wr 1 else begin a = 1; b = 1; i = 3; while i <= n do begin temp = a; a = a + b; b = temp; i = i +1 end; wr a end";

    // Parse and evaluate the program
    parser.parse(prg).eval(env);

    // Restore original System.out
    System.setOut(originalOut);

    // Convert the captured output to a string and trim to remove trailing newlines
    String output = outContent.toString().trim();

    // Assert that the captured output is the expected Fibonacci number
    assertEquals("55.0", output);
}

@Test
void testIfElse() throws SyntaxException, EvalException {
    String prg = "x = 5; if x > 10 then wr 1 else wr 0";
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outContent));

    parser.parse(prg).eval(env);

    System.setOut(originalOut);
    String output = outContent.toString().trim();
    assertEquals("0.0", output);
}

@Test
void testWhileLoop() throws SyntaxException, EvalException {
    String prg = "x = 0; while x < 5 do x = x + 1; wr x";
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outContent));

    parser.parse(prg).eval(env);

    System.setOut(originalOut);
    String output = outContent.toString().trim();
    assertEquals("5.0", output);
}

@Test
void testNestedLoops() throws SyntaxException, EvalException {
    String prg = "x = 0; y = 0; while x < 3 do begin x = x + 1; y = y + x end; wr y";
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outContent));

    parser.parse(prg).eval(env);

    System.setOut(originalOut);
    String output = outContent.toString().trim();
    assertEquals("6.0", output);
}


@Test
void testBooleanExpressions() throws SyntaxException, EvalException {
    String prg = "x = 5; y = 10; if x < y then wr 1 else wr 0";
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outContent));

    parser.parse(prg).eval(env);

    System.setOut(originalOut);
    String output = outContent.toString().trim();
    assertEquals("1.0", output);
}

@Test
void testIfBeginBlocks() throws SyntaxException, EvalException {
    String prg = "x = 10; if x > 5 then begin y = x - 5; wr y end else wr 0";
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outContent));

    parser.parse(prg).eval(env);

    System.setOut(originalOut);
    String output = outContent.toString().trim();
    assertEquals("5.0", output);
}

@Test
void testNestedWhileLoops() throws SyntaxException, EvalException {
    String prg = "x = 0; while x < 3 do begin x = x + 1; y = 0; while y < x do y = y + 1; wr y end";
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outContent));

    parser.parse(prg).eval(env);

    System.setOut(originalOut);
    String output = outContent.toString().trim();
    assertEquals("1.0\n2.0\n3.0", output);
}


}
