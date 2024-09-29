package ru.nsu.lebedev;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ExpressionParserTest {
    private Expression e;

    @BeforeEach
    public void setUp() throws Exception {
        e = new Add(new Add(new Number(3), new Mul(new Number(2),
            new Variable("x"))), new Variable("yxx"));
    }

    @Test
    void printExpressionParser() throws Exception {
        final PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        e = ExpressionParser.parse("(3+2)*x + 1 * 2");
        e.print();
        String output = outputStream.toString();
        assertTrue(output.contains("(((3+2)*x)+(1*2))"));
        System.setOut(originalOut);
    }
}


