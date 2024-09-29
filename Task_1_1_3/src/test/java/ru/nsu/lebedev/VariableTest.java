package ru.nsu.lebedev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VariableTest {
    private Expression variable;

    @BeforeEach
    public void setUp() throws Exception {
        variable = new Variable("x");
    }

    @Test
    void printVariable() throws Exception {
        final PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        variable.print();
        String output = outputStream.toString();
        assertTrue(output.contains("x"));
        System.setOut(originalOut);
    }

    @Test
    void derivativeVariable() throws Exception {
        final PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Expression de = variable.derivative("x");
        System.out.println();
        System.out.print("Derivative: ");
        de.print();
        String output = outputStream.toString();
        assertTrue(output.contains("Derivative: 1"));
        System.setOut(originalOut);
    }

    @Test
    void evalVariableAndDerivative() throws Exception {
        Expression de = variable.derivative("x");
        Map<String, Integer> variables = new HashMap<>();
        variables.put("x", 10);
        assertEquals(1, de.eval(variables));
        de = variable.derivative("y");
        variables = new HashMap<>();
        variables.put("x", 10);
        assertEquals(0, de.eval(variables));
        assertEquals(10, variable.eval("x = 10"));
    }
}


