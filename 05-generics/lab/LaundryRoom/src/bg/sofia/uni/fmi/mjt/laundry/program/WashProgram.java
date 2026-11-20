package bg.sofia.uni.fmi.mjt.laundry.program;

import bg.sofia.uni.fmi.mjt.laundry.garment.Garment;

public interface WashProgram<G extends Garment> {

    /**
     * @return the temperature of the program in degrees Celsius
     */
    int getTemperature();

    /**
     * Washes a single garment. After washing, the garment is clean.
     *
     * @param garment the garment to wash
     * @throws IllegalArgumentException if the garment is null
     */
    void wash(G garment);

}