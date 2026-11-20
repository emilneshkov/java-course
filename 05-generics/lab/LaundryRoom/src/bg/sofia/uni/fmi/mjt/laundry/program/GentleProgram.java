package bg.sofia.uni.fmi.mjt.laundry.program;

import bg.sofia.uni.fmi.mjt.laundry.garment.Garment;

public class GentleProgram extends AbstractWashProgram<Garment> {
    private static final int TEMPERATURE = 30;

    public GentleProgram() {
        super(TEMPERATURE);
    }
}
