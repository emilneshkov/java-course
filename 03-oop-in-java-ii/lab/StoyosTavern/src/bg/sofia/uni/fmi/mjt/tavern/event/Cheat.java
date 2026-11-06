package bg.sofia.uni.fmi.mjt.tavern.event;

public record Cheat(String cheater, String victim) implements Event {
    /**
     * One guest cheats another one at the card table.
     *
     * @throws IllegalArgumentException if any of the names is null or blank
     * @throws IllegalArgumentException if cheater and victim are the same name (names are compared exactly, case-sensitive)
     */

    public Cheat{
        Event.validateDifferentString(cheater,victim);
    }
}
