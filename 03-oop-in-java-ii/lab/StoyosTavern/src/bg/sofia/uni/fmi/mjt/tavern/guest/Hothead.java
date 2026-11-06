package bg.sofia.uni.fmi.mjt.tavern.guest;

public class Hothead extends Guest{
    private static final int TIPSY_THRESHOLD = 4;

    /**
     * @param name the name of the guest
     * @throws IllegalArgumentException if the name is null or blank
     */
    public Hothead(String name) {
        super(name);
    }

    @Override
    protected int getTipsyThreshold() {
        return TIPSY_THRESHOLD;
    }

    @Override
    public void onInsult() {
        setMood(Mood.BRAWLING);
    }

    @Override
    public void onCheat() {
        escalateMood();
        escalateMood();
    }

    @Override
    public void intervene(Guest victim) {
        victim.escalateMood();
    }
}
