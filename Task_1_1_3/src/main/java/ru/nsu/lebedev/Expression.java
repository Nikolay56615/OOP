package ru.nsu.lebedev;

import java.util.HashMap;
import java.util.Map;

// Абстрактный класс для всех выражений
abstract class Expression {
    // Метод для печати выражения
    public abstract void print();

    // Метод для дифференцирования выражения по заданной переменной
    public abstract Expression derivative(String variable);

    // Метод для вычисления значения выражения при подстановке переменных
    public abstract int eval(Map<String, Integer> variables);

    // Метод для парсинга строки и вызова eval с картой переменных
    public int eval(String variables) {
        Map<String, Integer> varMap = parseVariables(variables);
        return eval(varMap);
    }

    // Метод для парсинга строки с переменными
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
