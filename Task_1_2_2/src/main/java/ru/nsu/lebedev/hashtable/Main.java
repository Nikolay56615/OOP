package ru.nsu.lebedev.hashtable;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static int sum(int a, int b) {
        return a + b;
    }

    public static void main(String[] args) {
        HashTable<String, Number> hashTable = new HashTable<>();
        hashTable.put("one", 1);
        hashTable.update("one", 1.0);
        System.out.println(hashTable.get("one"));
        var remove_value = hashTable.remove("one");
        System.out.println(remove_value);
        System.out.println(hashTable.get("one"));
        System.out.println(hashTable);
        hashTable.put("one", 1);
        hashTable.put("two", 2);
        System.out.println(hashTable);
    }
}