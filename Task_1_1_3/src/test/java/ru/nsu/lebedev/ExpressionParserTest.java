package ru.nsu.lebedev;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Class for testing ExpressionParser class.
 */
public class ExpressionParserTest {
    private Expression expression;

    @BeforeEach
    public void setUp() throws Exception {
        expression = new Add(new Add(new Number(3), new Mul(new Number(2),
            new Variable("x"))), new Variable("yxx"));
    }

    @Test
    void printExpressionParser() throws Exception {
        final PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        expression = ExpressionParser.parse("(3+2)*x + 1 * 2");
        expression.print();
        String output = outputStream.toString();
        assertTrue(output.contains("(((3+2)*x)+(1*2))"));
        System.setOut(originalOut);
    }
}


