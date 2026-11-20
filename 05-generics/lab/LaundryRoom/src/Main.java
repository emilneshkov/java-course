import bg.sofia.uni.fmi.mjt.laundry.LaundryRoom;
import bg.sofia.uni.fmi.mjt.laundry.LaundryRoomAPI;
import bg.sofia.uni.fmi.mjt.laundry.basket.LaundryBasket;
import bg.sofia.uni.fmi.mjt.laundry.garment.Cotton;
import bg.sofia.uni.fmi.mjt.laundry.garment.Sweater;
import bg.sofia.uni.fmi.mjt.laundry.garment.TShirt;
import bg.sofia.uni.fmi.mjt.laundry.garment.Towel;
import bg.sofia.uni.fmi.mjt.laundry.program.CottonProgram;
import bg.sofia.uni.fmi.mjt.laundry.program.GentleProgram;

private static final double MARIA_BASKET_CAPACITY = 3.0;
private static final double IVAN_BASKET_CAPACITY = 3.0;
private static final double ROOM_BASKET_CAPACITY = 2.0;

private static final double SWEATER_WEIGHT = 0.75;
private static final double TSHIRT_WEIGHT = 0.25;
private static final double TOWEL_WEIGHT = 1.0;

private static final int EXPECTED_SCHEDULED_WASHES = 2;
private static final int EXPECTED_ROOM_TOKENS = 1;
private static final int EXPECTED_IVAN_TOKENS = 0;

void main() {
    LaundryRoomAPI block60 = new LaundryRoom();

    LaundryBasket<Sweater> mariasSweaters = new LaundryBasket<>("Мария", MARIA_BASKET_CAPACITY);

    Sweater sweater = new Sweater("Reserved", SWEATER_WEIGHT);
    mariasSweaters.add(sweater);

    LaundryBasket<TShirt> ivansShirts = new LaundryBasket<>("Иван", IVAN_BASKET_CAPACITY);

    ivansShirts.addAll(List.of(new TShirt("Nike", TSHIRT_WEIGHT), new TShirt("Adidas", TSHIRT_WEIGHT)));

    LaundryBasket<Cotton> room412 = new LaundryBasket<>("стая 412", ROOM_BASKET_CAPACITY);

    ivansShirts.moveAllTo(room412);
    room412.add(new Towel("IKEA", TOWEL_WEIGHT));

    block60.scheduleWash(mariasSweaters, new GentleProgram());
    block60.scheduleWash(room412, new CottonProgram());

// Не се компилира:
// block60.scheduleWash(mariasSweaters, new CottonProgram());

    block60.getScheduledWashes().size(); // EXPECTED_SCHEDULED_WASHES
    block60.runNext();
    sweater.isClean(); // true
    block60.runNext();
    block60.getTokensSpent("стая 412"); // EXPECTED_ROOM_TOKENS
    block60.getTokensSpent("Иван");     // EXPECTED_IVAN_TOKENS
}
