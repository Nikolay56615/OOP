package ru.nsu.lebedev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NumberTest {
    private Expression number;

    @BeforeEach
    public void setUp() throws Exception {
        number = new Number(5);
    }

    @Test
    void printNumber() throws Exception {
        final PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        number.print();
        String output = outputStream.toString();
        assertTrue(output.contains("5"));
        System.setOut(originalOut);
    }

    @Test
    void derivativeNumber() throws Exception {
        final PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Expression de = number.derivative("x");
        System.out.println();
        System.out.print("Derivative: ");
        de.print();
        String output = outputStream.toString();
        assertTrue(output.contains("Derivative: 0"));
        System.setOut(originalOut);
    }

    @Test
    void evalNumberAndDerivative() throws Exception {
        Expression de = number.derivative("x");
        Map<String, Integer> variables = new HashMap<>();
        variables.put("x", 10);
        assertEquals(0, de.eval(variables));
        assertEquals(5, number.eval("x = 10"));
    }
}


