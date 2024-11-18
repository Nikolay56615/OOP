package ru.nsu.lebedev.stringfinder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class Main {
    /**
     * Main function.
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Окай");

        InputStream resourceInputStream = Main.class.getClassLoader().getResourceAsStream("russian.txt");
        assert resourceInputStream != null;
        InputStreamReader inputStreamReader = new InputStreamReader(resourceInputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        char[] charBuffer = new char[1024];
        int data;
        while (true) {
            try {
                if ((data = reader.read(charBuffer)) == -1) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.print(data);
            System.out.print(charBuffer);
        }
        resourceInputStream.close();
    }
}