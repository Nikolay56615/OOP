package ru.nsu.lebedev;

import java.util.Map;

/**
 * Class with division realisation.
 */
public class Div extends Expression {
    private Expression left;
    private Expression right;

    /**
     * Function with initialization.
     */
    public Div(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * A method for formating div.
     */
    @Override
    public String toString() {
        return "(" + left.toString() + "/"
                + right.toString() + ")";
    }

    /**
     * Function with derivative of division.
     * By rule: (f / g)' = (f' * g - f * g') / g^2.
     */
    @Override
    public Expression derivative(String variable) {
        return new Div(
            new Sub(new Mul(left.derivative(variable), right),
                new Mul(left, right.derivative(variable))),
            new Mul(right, right)
        );
    }

    /**
     * Function with eval of division.
     */
    @Override
    public int eval(Map<String, Integer> env) {
        return left.eval(env) / right.eval(env);
    }
}
