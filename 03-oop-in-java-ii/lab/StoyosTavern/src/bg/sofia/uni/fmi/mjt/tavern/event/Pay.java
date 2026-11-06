package bg.sofia.uni.fmi.mjt.tavern.event;

public record Pay(String guest, int amount) implements Event {
    /**
     * A guest pays a part of the tab.
     *
     * @throws IllegalArgumentException if the guest is null or blank
     * @throws IllegalArgumentException if the amount is not positive
     */

    public Pay{
        Event.validateString(guest);
        Event.validateNumber(amount);
    }
}
