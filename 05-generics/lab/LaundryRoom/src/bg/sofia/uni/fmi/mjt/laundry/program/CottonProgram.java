package bg.sofia.uni.fmi.mjt.laundry.program;

import bg.sofia.uni.fmi.mjt.laundry.garment.Cotton;

public class CottonProgram extends AbstractWashProgram<Cotton> {
    private static final int TEMPERATURE = 90;

    public CottonProgram() {
        super(TEMPERATURE);
    }
}
