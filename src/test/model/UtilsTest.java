package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UtilsTest {
    @Test
    void testLow() {
        assertEquals(1, Utils.clamp(0, 1, 2));
    }

    @Test
    void testHigh() {
        assertEquals(2, Utils.clamp(4, 1, 2));
    }

    @Test
    void testWithin() {
        assertEquals(1, Utils.clamp(1, 0, 2));
    }
}
