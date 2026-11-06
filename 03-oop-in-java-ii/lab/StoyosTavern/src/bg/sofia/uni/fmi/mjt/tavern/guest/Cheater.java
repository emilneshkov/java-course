package bg.sofia.uni.fmi.mjt.tavern.guest;

public class Cheater extends Guest {
    private static final int TIPSY_THRESHOLD = 3;

    /**
     * @param name the name of the guest
     * @throws IllegalArgumentException if the name is null or blank
     */
    public Cheater(String name) {
        super(name);
    }

    @Override
    protected int getTipsyThreshold() {
        return TIPSY_THRESHOLD;
    }

    @Override
    public void onInsult() {
        escalateMood();
    }

    @Override
    public void onCheat() {
        calmDownMood();
    }

    @Override
    public void intervene(Guest victim) {
        //Does nothing
    }
}
