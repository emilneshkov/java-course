package bg.sofia.uni.fmi.mjt.tavern.guest;

public class Drunkard extends Guest {
    private static final int TIPSY_THRESHOLD = 2;

    /**
     * @param name the name of the guest
     * @throws IllegalArgumentException if the name is null or blank
     */
    public Drunkard(String name) {
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
        //Does not change his mood
    }

    @Override
    public void intervene(Guest victim) {
        //Does nothing
    }
}
