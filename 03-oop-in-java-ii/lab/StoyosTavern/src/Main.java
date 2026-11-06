import bg.sofia.uni.fmi.mjt.tavern.TavernAPI;
import bg.sofia.uni.fmi.mjt.tavern.TavernAPIImpl;
import bg.sofia.uni.fmi.mjt.tavern.event.*;
import bg.sofia.uni.fmi.mjt.tavern.exception.BrawlException;
import bg.sofia.uni.fmi.mjt.tavern.guest.Cheater;
import bg.sofia.uni.fmi.mjt.tavern.guest.Drunkard;
import bg.sofia.uni.fmi.mjt.tavern.guest.Hothead;
import bg.sofia.uni.fmi.mjt.tavern.guest.Peacemaker;

void main(){
    TavernAPI tavern = new TavernAPIImpl(2);

    tavern.admit(new Drunkard("Митко"));
    tavern.admit(new Hothead("Пешо"));
    tavern.admit(new Peacemaker("Мариян"));
    tavern.admit(new Cheater("Гошо"));

    tavern.handle(new Drink("Митко", 3));
    tavern.moodOf("Митко"); // CALM (пиенето не променя настроението)
    tavern.tabOf("Митко");  // 6
// 3 ракии са над прага 2 за Drunkard, така че Митко вече е пийнал

    tavern.handle(new Drink("Пешо", 1));
    tavern.moodOf("Пешо");  // CALM (1 ракия е под прага 4, Пешо е трезвен)

    tavern.handle(new Insult("Пешо", "Митко"));
    tavern.moodOf("Митко"); // TENSE
// Drunkard реагира на обида с escalate(): CALM -> TENSE.
// Митко е пийнал и реакцията го е влошила, затова още една стъпка: TENSE -> ANGRY.
// Мариян се намесва: calmDown() -> TENSE. Гошо не прави нищо.

    tavern.handle(new Insult("Митко", "Пешо"));
    tavern.moodOf("Пешо");  // ANGRY
// Пешо е Hothead и от обидата веднага става BRAWLING
// Мариян го успокоява: calmDown() -> ANGRY.
// Боят е предотвратен.

    tavern.handle(new Leave("Мариян")); // няма сметка, излиза тихо
    tavern.guestsInside(); // [Митко, Пешо, Гошо]

    try {
        tavern.handle(new Cheat("Гошо", "Пешо"));
        // Пешо е ANGRY и от мамене escalate() два пъти го прави BRAWLING.
        // Този път Мариян го няма и никой не го успокоява.
    } catch (BrawlException e) {
        System.out.println(e.getCause().getClass().getSimpleName()); // CheatingException
    }

    tavern.guestsInside(); // [Митко]
    tavern.bannedGuests(); // [Гошо, Пешо]

    tavern.admit(new Hothead("Пешо")); // BannedGuestException
    tavern.admit(new Drunkard("Митко")); // GuestAlreadyInsideException

    tavern.handle(new Pay("Митко", 4));
    tavern.tabOf("Митко"); // 2

    tavern.handle(new Leave("Митко")); // UnpaidTabException
    tavern.bannedGuests();             // [Гошо, Пешо, Митко]
    tavern.guestsInside();             // []
}

