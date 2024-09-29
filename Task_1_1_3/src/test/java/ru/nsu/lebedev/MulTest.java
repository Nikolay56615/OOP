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
 * Class for testing Mul class.
 */
public class MulTest {
    private Expression mul;

    @BeforeEach
    public void setUp() throws Exception {
        mul = new Mul(new Number(5), new Variable("x"));
    }

    @Test
    void printMul() throws Exception {
        final PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        mul.print();
        String output = outputStream.toString();
        assertTrue(output.contains("(5*x)"));
        System.setOut(originalOut);
    }

    @Test
    void derivativeMul() throws Exception {
        final PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Expression de = mul.derivative("x");
        System.out.println();
        System.out.print("Derivative: ");
        de.print();
        String output = outputStream.toString();
        assertTrue(output.contains("Derivative: ((0*x)+(5*1))"));
        System.setOut(originalOut);
    }

    @Test
    void evalMulAndDerivative() throws Exception {
        Expression de = mul.derivative("x");
        Map<String, Integer> variables = new HashMap<>();
        variables.put("x", 10);
        assertEquals(5, de.eval(variables));
        assertEquals(50, mul.eval("x = 10"));
    }
}

