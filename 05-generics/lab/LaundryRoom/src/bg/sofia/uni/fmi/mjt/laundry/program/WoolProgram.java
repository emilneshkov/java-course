package bg.sofia.uni.fmi.mjt.laundry.program;

import bg.sofia.uni.fmi.mjt.laundry.garment.Wool;

public class WoolProgram extends AbstractWashProgram<Wool> {
    private static final int TEMPERATURE = 40;

    public WoolProgram() {
        super(TEMPERATURE);
    }
}
