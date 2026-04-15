package com.electricity;

/**
 * Smart Electricity Billing System
 * Calculates bill based on slab rates (domestic tariff example).
 */
public class BillingCalculator {

    // Tariff slabs (units consumed, rate per unit in paise/cents)
    // Example: 0-100: ₹3.00, 101-200: ₹5.00, 201-500: ₹7.00, >500: ₹9.00
    private static final int[] SLAB_LIMITS = {100, 200, 500, Integer.MAX_VALUE};
    private static final double[] SLAB_RATES = {3.00, 5.00, 7.00, 9.00}; // in rupees
    private static final double FIXED_CHARGE = 50.00; // fixed monthly charge

    /**
     * Calculate total bill amount based on units consumed.
     * @param unitsConsumed number of units (kWh)
     * @return total bill amount in rupees (rounded to 2 decimals)
     * @throws IllegalArgumentException if unitsConsumed < 0
     */
    public static double calculateBill(double unitsConsumed) {
        if (unitsConsumed < 0) {
            throw new IllegalArgumentException("Units consumed cannot be negative");
        }

        double energyCharge = 0.0;
        double remaining = unitsConsumed;
        double prevLimit = 0;

        for (int i = 0; i < SLAB_LIMITS.length; i++) {
            int limit = SLAB_LIMITS[i];
            double slabUnits = Math.min(remaining, limit - prevLimit);
            if (slabUnits > 0) {
                energyCharge += slabUnits * SLAB_RATES[i];
                remaining -= slabUnits;
            }
            prevLimit = limit;
            if (remaining <= 0) break;
        }

        double total = energyCharge + FIXED_CHARGE;
        // Round to 2 decimal places
        return Math.round(total * 100.0) / 100.0;
    }

    /**
     * Get detailed breakdown of bill calculation.
     * @param unitsConsumed units
     * @return formatted string with slab details and total
     */
    public static String getBillBreakdown(double unitsConsumed) {
        if (unitsConsumed < 0) {
            return "Invalid units";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Units Consumed: %.2f kWh\n", unitsConsumed));
        sb.append(String.format("Fixed Charge: ₹%.2f\n", FIXED_CHARGE));

        double energyCharge = 0.0;
        double remaining = unitsConsumed;
        double prevLimit = 0;
        sb.append("Slab Details:\n");

        for (int i = 0; i < SLAB_LIMITS.length; i++) {
            int limit = SLAB_LIMITS[i];
            double slabUnits = Math.min(remaining, limit - prevLimit);
            if (slabUnits > 0) {
                double slabCost = slabUnits * SLAB_RATES[i];
                energyCharge += slabCost;
                sb.append(String.format("  %.2f units @ ₹%.2f = ₹%.2f\n", slabUnits, SLAB_RATES[i], slabCost));
                remaining -= slabUnits;
            }
            prevLimit = limit;
            if (remaining <= 0) break;
        }

        double total = energyCharge + FIXED_CHARGE;
        sb.append(String.format("Energy Charge: ₹%.2f\n", energyCharge));
        sb.append(String.format("Total Bill: ₹%.2f\n", Math.round(total * 100.0) / 100.0));
        return sb.toString();
    }

    public static void main(String[] args) {
        // Demo
        double units = 350;
        System.out.println(getBillBreakdown(units));
        System.out.println("Total: ₹" + calculateBill(units));
    }
}