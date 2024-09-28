package ru.nsu.lebedev;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    @Test
    void test() {
        assertEquals(5, Main.sum(2, 3));
    }
}