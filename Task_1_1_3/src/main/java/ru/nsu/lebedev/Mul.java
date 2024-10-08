package ru.nsu.lebedev;

import java.util.Map;

/**
 * Class with multiplication realisation.
 */
public class Mul extends Expression {
    private Expression left;
    private Expression right;

    /**
     * Function with initialization.
     */
    public Mul(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * A method for formating mul.
     */
    @Override
    public String toString() {
        return "(" + left.toString() + "*" + right.toString() + ")";
    }

    /**
     * Function with derivative of multiplication.
     * By rule: (f * g)' = f' * g + f * g'.
     */
    @Override
    public Expression derivative(String variable) {
        return new Add(new Mul(left.derivative(variable), right),
            new Mul(left, right.derivative(variable)));
    }

    /**
     * Function with eval of multiplication.
     */
    @Override
    public int eval(Map<String, Integer> env) {
        return left.eval(env) * right.eval(env);
    }
}
