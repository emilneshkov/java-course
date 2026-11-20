package bg.sofia.uni.fmi.mjt.laundry.program;

import bg.sofia.uni.fmi.mjt.laundry.garment.Garment;

public abstract class AbstractWashProgram<G extends Garment> implements WashProgram<G> {
    private final int temperature;

    protected AbstractWashProgram(int temperature) {
        this.temperature = temperature;
    }

    @Override
    public int getTemperature() {
        return temperature;
    }

    @Override
    public void wash(G garment) {
        if (garment == null) {
            throw new IllegalArgumentException("Garment cannot be null");
        }

        garment.markClean();
    }
}
