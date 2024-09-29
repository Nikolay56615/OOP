package ru.nsu.lebedev;

import java.util.Map;

/**
 * Class with constant realisation.
 */
public class Number extends Expression {
    private int value;

    /**
     * Function with initialization.
     */
    public Number(int value) {
        this.value = value;
    }

    /**
     * Function with printing constant.
     */
    @Override
    public void print() {
        System.out.print(value);
    }

    /**
     * Function with derivative of constant.
     */
    @Override
    public Expression derivative(String variable) {
        return new Number(0);
    }

    /**
     * Function with eval of constant.
     */
    @Override
    public int eval(Map<String, Integer> var) {
        return value;
    }
}
