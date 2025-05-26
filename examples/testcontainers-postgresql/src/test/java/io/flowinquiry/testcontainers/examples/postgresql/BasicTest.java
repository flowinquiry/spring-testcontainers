package io.flowinquiry.testcontainers.examples.postgresql;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BasicTest {

    @Test
    public void simpleTest() {
        System.out.println("[DEBUG_LOG] Running simple test");
        assertTrue(true);
        System.out.println("[DEBUG_LOG] Simple test passed");
    }
}