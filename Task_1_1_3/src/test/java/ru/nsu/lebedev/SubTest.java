package ru.nsu.lebedev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SubTest {
    private Expression sub;

    @BeforeEach
    public void setUp() throws Exception {
        sub = new Sub(new Number(5), new Variable("x"));
    }

    @Test
    void printSub() throws Exception {
        final PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        sub.print();
        String output = outputStream.toString();
        assertTrue(output.contains("(5-x)"));
        System.setOut(originalOut);
    }

    @Test
    void derivativeSub() throws Exception {
        final PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Expression de = sub.derivative("x");
        System.out.println();
        System.out.print("Derivative: ");
        de.print();
        String output = outputStream.toString();
        assertTrue(output.contains("Derivative: (0-1)"));
        System.setOut(originalOut);
    }

    @Test
    void evalSubAndDerivative() throws Exception {
        Expression de = sub.derivative("x");
        Map<String, Integer> variables = new HashMap<>();
        variables.put("x", 10);
        assertEquals(-1, de.eval(variables));
        assertEquals(-5, sub.eval("x = 10"));
    }
}
