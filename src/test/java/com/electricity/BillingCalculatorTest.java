package com.electricity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class BillingCalculatorTest {

    @Test
    @DisplayName("Zero units should incur only fixed charge")
    void testZeroUnits() {
        assertEquals(50.00, BillingCalculator.calculateBill(0));
    }

    @Test
    @DisplayName("Negative units should throw exception")
    void testNegativeUnits() {
        assertThrows(IllegalArgumentException.class, () -> BillingCalculator.calculateBill(-10));
    }

    @ParameterizedTest
    @CsvSource({
        "50, 200.00",
        "100, 350.00",
        "150, 600.00",
        "200, 850.00",      // corrected
        "250, 1200.00",     // corrected
        "350, 1900.00",     // corrected
        "600, 3850.00"      // corrected
    })
    void testBillCalculation(double units, double expected) {
        double actual = BillingCalculator.calculateBill(units);
        assertEquals(expected, actual, 0.01);
    }

    @Test
    @DisplayName("Check exact slab boundaries")
    void testSlabBoundaries() {
        assertEquals(350.00, BillingCalculator.calculateBill(100));
        assertEquals(355.00, BillingCalculator.calculateBill(101));
        assertEquals(850.00, BillingCalculator.calculateBill(200));
        assertEquals(857.00, BillingCalculator.calculateBill(201));
    }

    @Test
    @DisplayName("Breakdown string contains expected text")
    void testGetBillBreakdown() {
        String breakdown = BillingCalculator.getBillBreakdown(150);
        assertTrue(breakdown.contains("Units Consumed: 150.00 kWh"));
        assertTrue(breakdown.contains("Fixed Charge: ₹50.00"));
        assertTrue(breakdown.contains("Total Bill: ₹600.00"));
    }
}