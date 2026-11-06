package bg.sofia.uni.fmi.mjt.tavern.guest;

public class Peacemaker extends Guest{
    private static final int TIPSY_THRESHOLD = 5;

    /**
     * @param name the name of the guest
     * @throws IllegalArgumentException if the name is null or blank
     */
    public Peacemaker(String name) {
        super(name);
    }

    @Override
    protected int getTipsyThreshold() {
        return TIPSY_THRESHOLD;
    }

    @Override
    public void onInsult() {
        //Does not change his mood
    }

    @Override
    public void onCheat() {
        //Does not change his mood
    }

    @Override
    public void intervene(Guest victim) {
        victim.calmDownMood();
    }
}
