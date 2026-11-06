package bg.sofia.uni.fmi.mjt.tavern.event;

public record Drink(String guest, int rakias) implements Event {
    /**
     * A guest orders rakias.
     *
     * @throws IllegalArgumentException if the guest is null or blank
     * @throws IllegalArgumentException if the rakias are not positive
     */

    public Drink {
        Event.validateString(guest);
        Event.validateNumber(rakias);
    }
}
