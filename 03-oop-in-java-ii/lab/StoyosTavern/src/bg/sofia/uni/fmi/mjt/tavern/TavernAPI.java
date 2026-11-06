package bg.sofia.uni.fmi.mjt.tavern;

import bg.sofia.uni.fmi.mjt.tavern.event.Event;
import bg.sofia.uni.fmi.mjt.tavern.exception.*;
import bg.sofia.uni.fmi.mjt.tavern.guest.Guest;
import bg.sofia.uni.fmi.mjt.tavern.guest.Mood;

public interface TavernAPI {

    /**
     * Lets a guest into the tavern. The guest starts the night with mood CALM,
     * no rakias drunk and an empty tab.
     * For simplicity, if a guest leaves and comes back, he is treated as a new guest,
     * with mood CALM, no rakias drunk and an empty tab.
     *
     * @param guest the guest who wants to come in
     * @throws IllegalArgumentException if the guest is null
     * @throws BannedGuestException if the name of the guest is in the list of the banned guests
     * @throws GuestAlreadyInsideException if there is already a guest with the same name inside the tavern
     */
    void admit(Guest guest);

    /**
     * Handles one event of the night. The rules for every kind of event are described below.
     * NoSuchGuestException and IllegalArgumentException leave the tavern unchanged.
     * BrawlException and UnpaidTabException are thrown after the tavern is updated.
     *
     * @param event the event that happens
     * @throws IllegalArgumentException if the event is null
     * @throws NoSuchGuestException if the event mentions a guest who is not inside the tavern
     * @throws BrawlException if the event ends with a brawl. The cause of the exception
     * is the exception that describes the provocation that started the brawl.
     * @throws UnpaidTabException if a guest leaves without paying the whole tab
     */
    void handle(Event event);

    /**
     * @param name the name of a guest who is inside the tavern
     * @return the current mood of the guest
     * @throws IllegalArgumentException if the name is null or blank
     * @throws NoSuchGuestException if there is no such guest inside the tavern
     */
    Mood moodOf(String name);

    /**
     * @param name the name of a guest who is inside the tavern
     * @return the current tab of the guest in euro cents (so we do not fight about the change)
     * @throws IllegalArgumentException if the name is null or blank
     * @throws NoSuchGuestException if there is no such guest inside the tavern
     */
    int tabOf(String name);

    /**
     * @return the names of the guests inside the tavern, in the order in which they were admitted.
     */
    String[] guestsInside();

    /**
     * @return the names of the banned guests, in the order in which they were banned.
     */
    String[] bannedGuests();

}