package bg.sofia.uni.fmi.mjt.laundry.garment;

public abstract class Garment {
    private final String brand;
    private final double weightKg;
    private boolean isClean;
    /**
     * @param brand    the brand of the garment
     * @param weightKg the weight of the garment in kilograms
     * @throws IllegalArgumentException if the brand is null or blank, or the weight is not positive
     */
    public Garment(String brand, double weightKg) {
        if (brand == null || brand.isBlank() || weightKg <= 0) {
            throw  new IllegalArgumentException("Invalid arguments");
        }

        this.brand = brand;
        this.weightKg = weightKg;
        isClean = false;
    }

    /**
     * @return the brand of the garment
     */
    public String getBrand() {
        return brand;
    }

    /**
     * @return the weight of the garment in kilograms
     */
    public double getWeightKg() {
        return  weightKg;
    }

    /**
     * @return true if the garment has been washed, false otherwise. A newly created garment is not clean.
     */
    public boolean isClean() {
        return isClean;
    }

    /**
     * Marks the garment as clean. Called by the wash programs.
     */
    public void markClean() {
        isClean = true;
    }

}