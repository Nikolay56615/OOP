package ru.nsu.lebedev;

import java.util.Map;

/**
 * Class with addition realisation.
 */
public class Add extends Expression {
    private Expression left;
    private Expression right;

    /**
     * Function with initialization.
     */
    public Add(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Function with printing addition.
     */
    @Override
    public void print() {
        System.out.print("(");
        left.print();
        System.out.print("+");
        right.print();
        System.out.print(")");
    }

    /**
     * Function with derivative of addition.
     */
    @Override
    public Expression derivative(String variable) {
        return new Add(left.derivative(variable), right.derivative(variable));
    }

    /**
     * Function with eval of addition.
     */
    @Override
    public int eval(Map<String, Integer> env) {
        return left.eval(env) + right.eval(env);
    }
}
