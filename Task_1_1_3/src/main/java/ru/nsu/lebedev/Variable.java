package ru.nsu.lebedev;

import java.util.Map;

/**
 * Class with variable realisation.
 */
public class Variable extends Expression {
    private String variable;

    /**
     * Function with initialization.
     */
    public Variable(String variable) {
        this.variable = variable;
    }

    /**
     * Function with printing variable.
     */
    @Override
    public void print() {
        System.out.print(variable);
    }

    /**
     * Function with derivative of variable.
     * by rule1: The derivative of the variable by itself is 1.
     * by rule2: The derivative of another variable is 0.
     */
    @Override
    public Expression derivative(String var) {
        if (variable.equals(var)) {
            return new Number(1);
        } else {
            return new Number(0);
        }
    }

    /**
     * Function with eval of variable.
     */
    @Override
    public int eval(Map<String, Integer> var){
        return var.getOrDefault(variable, 0);
    }
}
