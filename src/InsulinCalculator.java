public class InsulinCalculator {
    private static final double DEFAULT_ICR = 10.0;
    private static final double DEFAULT_CORRECTION_FACTOR = 50.0;

    private double insulinToCarbRatio;
    private double correctionFactor;

    public InsulinCalculator() {
        this.insulinToCarbRatio = DEFAULT_ICR;
        this.correctionFactor = DEFAULT_CORRECTION_FACTOR;
    }

    public double calculateInsulinForMeal(double carbGrams, double bloodGlucose) {
        double insulinForCarbs = carbGrams / insulinToCarbRatio;
        double insulinForCorrection = (bloodGlucose - 100) / correctionFactor; // Assuming target is 100 mg/dL

        return insulinForCarbs + insulinForCorrection;
    }
}
