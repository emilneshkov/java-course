package bg.sofia.uni.fmi.mjt.carwash.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WashSuppliesTest {

    @Test
    void testConstructorWithNegativeWater() {
        assertThrows(IllegalArgumentException.class,
                () -> new WashSupplies(-1, 100, 100),
                "Negative water should be rejected");
    }

    @Test
    void testConstructorWithNegativeShampoo() {
        assertThrows(IllegalArgumentException.class,
                () -> new WashSupplies(100, -1, 100),
                "Negative shampoo should be rejected");
    }

    @Test
    void testConstructorWithNegativeWax() {
        assertThrows(IllegalArgumentException.class,
                () -> new WashSupplies(100, 100, -1),
                "Negative wax should be rejected");
    }

    @Test
    void testConstructorWithZeroSupplies() {
        assertDoesNotThrow(
                () -> new WashSupplies(0, 0, 0),
                "Zero supplies should be allowed");
    }
}
