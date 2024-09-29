package ru.nsu.lebedev;

import java.util.HashMap;
import java.util.Map;

/**
 * Main class for project.
 */
public class Main {

    /**
     * Function with observing of classes.
     */
    public static void main(String[] args) {
        Expression e = new Add(new Add(new Number(3), new Mul(new Number(2),
            new Variable("x"))), new Variable("yxx"));
        System.out.print("Expression: ");
        e.print(); // (3+(2*x))
        Expression de = e.derivative("yxx");
        System.out.println();
        System.out.print("Derivative: ");
        de.print(); // (0+((0*x)+(2*1)))
        System.out.println();
        Map<String, Integer> variables = new HashMap<>();
        variables.put("x", 10);
        System.out.println("Evaluation: " + e.eval("x = 10; yxx = 13"));  // 36
        System.out.println("Evaluation: " + e.eval(variables));  // 23
        e = ExpressionParser.parse("(3+2)*x + 1 * 2"); // (((3+2)*x)+(1*2))
        e.print();
        variables = new HashMap<>();
        System.out.println("Evaluation: " + e.eval(variables));  // 2
    }
}