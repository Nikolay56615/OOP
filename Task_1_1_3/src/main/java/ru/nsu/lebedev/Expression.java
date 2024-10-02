package ru.nsu.lebedev;

import java.util.HashMap;
import java.util.Map;

/**
 * An abstract class for all expressions.
 */
abstract class Expression {

    /**
     * A method for printing an expression.
     */
    public abstract void print();

    /**
     * A method for formating an expression.
     */
    public abstract String toString();

    /**
     * A method for differentiating an expression by a given variable.
     */
    public abstract Expression derivative(String variable);

    /**
     * A method for calculating the value of a expression when substituting variables.
     */
    public abstract int eval(Map<String, Integer> variables);

    /**
     * A method for parsing a string and calling eval with a variable map.
     */
    public int eval(String variables) {
        Map<String, Integer> varMap = parseVariables(variables);
        return eval(varMap);
    }

    /**
     * Method for parsing a string with variables
     */
    private Map<String, Integer> parseVariables(String input) {
        Map<String, Integer> variables = new HashMap<>();
        String[] assignments = input.split(";");
        for (String assignment : assignments) {
            String[] parts = assignment.split("=");
            if (parts.length == 2) {
                String varName = parts[0].trim();
                int value = Integer.parseInt(parts[1].trim());
                variables.put(varName, value);
            }
        }
        return variables;
    }
}
