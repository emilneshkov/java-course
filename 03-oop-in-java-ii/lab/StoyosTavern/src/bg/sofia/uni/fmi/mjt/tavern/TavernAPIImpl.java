package bg.sofia.uni.fmi.mjt.tavern;

import bg.sofia.uni.fmi.mjt.tavern.event.*;
import bg.sofia.uni.fmi.mjt.tavern.exception.*;
import bg.sofia.uni.fmi.mjt.tavern.guest.Guest;
import bg.sofia.uni.fmi.mjt.tavern.guest.Mood;

public class TavernAPIImpl implements TavernAPI {
    private static final int INITIAL_CAPACITY = 10;

    private final int rakiaPrice;

    private Guest[] guests;
    private int guestCount;

    private String[] banned;
    private int bannedCount;

    /**
     * @param rakiaPrice the price of one rakia in euro cents
     * @throws IllegalArgumentException if the rakiaPrice is not positive
     */
    public TavernAPIImpl(int rakiaPrice) {
        if (rakiaPrice <= 0) {
            throw new IllegalArgumentException("Invalid rakia price");
        }

        this.rakiaPrice = rakiaPrice;

        this.guests = new Guest[INITIAL_CAPACITY];
        this.banned = new String[INITIAL_CAPACITY];
    }

    private Guest findGuest(String name) {
        for (int i = 0; i < guestCount; i++) {
            if (guests[i].getName().equals(name)) {
                return guests[i];
            }
        }

        return null;
    }

    private void resizeGuests() {
        Guest[] newGuests = new Guest[guests.length * 2];

        for (int i = 0; i < guestCount; i++) {
            newGuests[i] = guests[i];
        }

        guests = newGuests;
    }

    private boolean isBanned(String name) {
        for (int i = 0; i < bannedCount; i++) {
            if (banned[i].equals(name)) {
                return true;
            }
        }

        return false;
    }

    private  void resizeBannedGuests(){
        String[] newBanned = new String[banned.length * 2];

        for (int i = 0; i < bannedCount; i++) {
            newBanned[i] = banned[i];
        }

        banned = newBanned;
    }

    private void removeGuest(Guest guest) {
        for (int i = 0; i < guestCount; i++) {
            if (guests[i] == guest) {
                for (int j = i; j < guestCount - 1; j++) {
                    guests[j] = guests[j + 1];
                }

                guests[--guestCount] = null;
                return;
            }
        }
    }

    private void banGuest(Guest guest) {
        if (bannedCount == banned.length) {
            resizeBannedGuests();
        }

        banned[bannedCount++] = guest.getName();

        removeGuest(guest);
    }

    @Override
    public void admit(Guest guest) {
        if (guest == null) {
            throw new IllegalArgumentException("Guest cannot be null");
        }

        if (isBanned(guest.getName())) {
            throw new BannedGuestException("Guest is banned");
        }

        if (findGuest(guest.getName()) != null) {
            throw new GuestAlreadyInsideException("Guest already inside");
        }

        if (guestCount == guests.length) {
            resizeGuests();
        }

        guest.reset();

        guests[guestCount] = guest;
        guestCount++;
    }

    @Override
    public void handle(Event event) {
        if(event == null){
            throw new IllegalArgumentException("Event cannot be null");
        }

        switch (event) {
            case Drink drink -> handleDrink(drink);
            case Insult insult -> handleInsult(insult);
            case Cheat cheat -> handleCheat(cheat);
            case Pay pay -> handlePay(pay);
            case Leave leave -> handleLeave(leave);
        }
    }

    private void handleLeave(Leave leave) {
        Guest guest = findGuest(leave.guest());

        if (guest == null) {
            throw new NoSuchGuestException("Guest not found");
        }

        if (guest.getTab() > 0) {
            banGuest(guest);
            throw new UnpaidTabException("Guest left without paying");
        }

        removeGuest(guest);
    }

    private void handlePay(Pay pay) {
        Guest guest = findGuest(pay.guest());

        if (guest == null) {
            throw new NoSuchGuestException("Guest not found");
        }

        guest.pay(pay.amount());
    }

    private void handleProvocation(String initiatorName, String victimName, ProvocationType type) {
        Guest initiator = findGuest(initiatorName);
        Guest victim = findGuest(victimName);

        if (initiator == null || victim == null) {
            throw new NoSuchGuestException("Guest not found");
        }

        switch (type) {
            case INSULT -> victim.reactToInsult();
            case CHEAT -> victim.reactToCheat();
        }

        for (int i = 0; i < guestCount; i++) {
            Guest current = guests[i];

            if (current != initiator && current != victim) {
                current.intervene(victim);
            }
        }

        if (victim.getMood() == Mood.BRAWLING) {
            banGuest(initiator);
            banGuest(victim);

            ProvocationException cause = switch (type) {
                case INSULT -> new InsultException("Brawl caused by insult");
                case CHEAT -> new CheatingException("Brawl caused by cheating");
            };

            throw new BrawlException(cause);
        }
    }

    private void handleInsult(Insult insult) {
        handleProvocation(insult.from(), insult.to(), ProvocationType.INSULT);
    }

    private void handleCheat(Cheat cheat) {
        handleProvocation(cheat.cheater(), cheat.victim(), ProvocationType.CHEAT);
    }

    private void handleDrink(Drink drink) {
        Guest guest = findGuest(drink.guest());

        if (guest == null) {
            throw new NoSuchGuestException("Guest not found");
        }

        guest.drink(drink.rakias(), rakiaPrice);
    }

    @Override
    public Mood moodOf(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Invalid name");
        }

        Guest guest = findGuest(name);

        if (guest == null) {
            throw new NoSuchGuestException("Guest not found");
        }

        return guest.getMood();
    }

    @Override
    public int tabOf(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Invalid name");
        }

        Guest guest = findGuest(name);

        if (guest == null) {
            throw new NoSuchGuestException("Guest not found");
        }

        return guest.getTab();
    }

    @Override
    public String[] guestsInside() {
        String[] result = new String[guestCount];

        for (int i = 0; i < guestCount; i++) {
            result[i] = guests[i].getName();
        }

        return result;
    }

    @Override
    public String[] bannedGuests() {
        String[] result = new String[bannedCount];

        for (int i = 0; i < bannedCount; i++) {
            result[i] = banned[i];
        }

        return result;
    }
}
