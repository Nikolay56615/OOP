package ru.nsu.lebedev;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class MainTest {
    @Test
    void callMain() {
        Main.main(new String[]{});
        assertTrue(true);
    }
}