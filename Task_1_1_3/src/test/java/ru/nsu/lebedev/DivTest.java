package ru.nsu.lebedev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Class for testing Div class.
 */
public class DivTest {
    private Expression div;

    @BeforeEach
    public void setUp() throws Exception {
        div = new Div(new Number(5), new Variable("x"));
    }

    @Test
    void printDiv() throws Exception {
        final PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        div.print();
        String output = outputStream.toString();
        assertTrue(output.contains("(5/x)"));
        System.setOut(originalOut);
    }

    @Test
    void derivativeDiv() throws Exception {
        final PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Expression de = div.derivative("x");
        System.out.println();
        System.out.print("Derivative: ");
        de.print();
        String output = outputStream.toString();
        assertTrue(output.contains("Derivative: (((0*x)-(5*1))/(x*x))"));
        System.setOut(originalOut);
    }

    @Test
    void evalDivAndDerivative() throws Exception {
        Expression de = div.derivative("x");
        Map<String, Integer> variables = new HashMap<>();
        variables.put("x", 10);
        assertEquals(0, de.eval(variables));
        assertEquals(0, div.eval("x = 10"));
    }
}

