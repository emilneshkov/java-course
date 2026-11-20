package bg.sofia.uni.fmi.mjt.laundry;

import bg.sofia.uni.fmi.mjt.laundry.basket.LaundryBasket;
import bg.sofia.uni.fmi.mjt.laundry.exception.AlreadyQueuedException;
import bg.sofia.uni.fmi.mjt.laundry.exception.EmptyQueueException;
import bg.sofia.uni.fmi.mjt.laundry.garment.Garment;
import bg.sofia.uni.fmi.mjt.laundry.program.WashProgram;

import java.util.List;

public interface LaundryRoomAPI {

    /**
     * Puts the basket at the back of the queue to be washed with the given program.
     *
     * @param basket  the basket to wash
     * @param program the program to wash it with
     * @return the wash job that was put in the queue
     * @throws IllegalArgumentException if the basket or the program is null, or the basket is empty
     * @throws AlreadyQueuedException   if the same basket object is already waiting in the queue
     */
    <G extends Garment> WashJob<G> scheduleWash(LaundryBasket<G> basket, WashProgram<? super G> program);

    /**
     * Cancels the scheduled wash for the given basket and removes it from the queue.
     *
     * @param basket the basket whose wash to cancel
     * @return true if the given basket object was waiting in the queue and was removed, false otherwise
     * @throws IllegalArgumentException if the basket is null
     */
    boolean cancelWash(LaundryBasket<?> basket);

    /**
     * Runs the washing machine with the wash job at the front of the queue. Every run costs one token,
     * paid by the owner of the basket.
     *
     * @return the completed wash job
     * @throws EmptyQueueException if the queue is empty
     */
    WashJob<?> runNext();

    /**
     * @return an unmodifiable copy of the wash jobs waiting in the queue, in the order in which they will be washed
     */
    List<WashJob<?>> getScheduledWashes();

    /**
     * @param owner the person whose tokens are counted
     * @return the number of tokens the owner has paid so far; 0 if the owner has not washed anything
     * @throws IllegalArgumentException if the owner is null or blank
     */
    int getTokensSpent(String owner);

}