package bg.sofia.uni.fmi.mjt.tavern.guest;

public abstract class Guest {
    private  final String name;
    private Mood mood;
    private int rakiasDrunk;
    private int tab;

    /**
     * @param name the name of the guest
     * @throws IllegalArgumentException if the name is null or blank
     */
    public Guest(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException();
        }

        this.name = name;
        this.mood = Mood.CALM;
    }

    /**
     * @return the name of the guest
     */
    public String getName() {
        return name;
    }

    public Mood getMood() {
        return mood;
    }

    protected  void setMood(Mood newMood){
        mood = newMood;
    }

    protected void escalateMood() {
        mood = mood.escalate();
    }

    protected void calmDownMood() {
        mood = mood.calmDown();
    }

    public void reset() {
        mood = Mood.CALM;
        rakiasDrunk = 0;
        tab = 0;
    }

    public void drink(int rakias, int rakiaPrice) {
        rakiasDrunk += rakias;
        tab += rakias * rakiaPrice;
    }

    private void applyTipsyEffect(Mood previousMood) {
        if (isTipsy() && mood.ordinal() > previousMood.ordinal()) {
            escalateMood();
        }
    }

    public void reactToInsult() {
        Mood previousMood = mood;
        onInsult();
        applyTipsyEffect(previousMood);
    }

    public void reactToCheat() {
        Mood previousMood = mood;
        onCheat();
        applyTipsyEffect(previousMood);
    }

    public void pay(int amount) {
        tab = Math.max(0, tab - amount);
    }

    public int getTab() {
        return tab;
    }

    public boolean isTipsy() {
        return rakiasDrunk >= getTipsyThreshold();
    }

    protected abstract int getTipsyThreshold();

    public abstract void onInsult();

    public abstract void onCheat();

    public abstract void intervene(Guest victim);
}
