package ru.nsu.lebedev;

import java.util.Map;

/**
 * Class with subtraction realisation.
 */
public class Sub extends Expression {
    private Expression left;
    private Expression right;

    /**
     * Function with initialization.
     */
    public Sub(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * A method for formating sub.
     */
    @Override
    public String toString() {
        return "(" + left.toString() + "-" + right.toString() + ")";
    }

    /**
     * Function with derivative of subtraction.
     */
    @Override
    public Expression derivative(String variable) {
        return new Sub(left.derivative(variable), right.derivative(variable));
    }

    /**
     * Function with eval of subtraction.
     */
    @Override
    public int eval(Map<String, Integer> env) {
        return left.eval(env) - right.eval(env);
    }
}
