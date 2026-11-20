package bg.sofia.uni.fmi.mjt.laundry;

import bg.sofia.uni.fmi.mjt.laundry.basket.LaundryBasket;
import bg.sofia.uni.fmi.mjt.laundry.garment.Garment;
import bg.sofia.uni.fmi.mjt.laundry.program.WashProgram;

/**
 * A basket registered for washing, together with the program it will be washed with.
 */
public record WashJob<G extends Garment>(LaundryBasket<G> basket, WashProgram<? super G> program) {

    /**
     * @throws IllegalArgumentException if the basket or the program is null
     */
    public WashJob {
        if (basket == null || program == null ) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
    }

    /**
     * Washes every garment currently in the basket with the program. The garments remain in the basket.
     */
    public void wash() {
        for (G garment : basket) {
            program.wash(garment);
        }
    }
}