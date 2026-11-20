package bg.sofia.uni.fmi.mjt.laundry.basket;

import bg.sofia.uni.fmi.mjt.laundry.exception.BasketOverloadedException;
import bg.sofia.uni.fmi.mjt.laundry.exception.EmptyBasketException;
import bg.sofia.uni.fmi.mjt.laundry.garment.Garment;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

public class LaundryBasket<G extends Garment> implements Iterable<G> {

    private final String owner;
    private final double capacityKg;
    private double currentWeightKg;
    private final Deque<G> garments = new ArrayDeque<>();

    /**
     * @param owner      the owner of the basket
     * @param capacityKg the maximum total weight of the garments in the basket, in kilograms
     * @throws IllegalArgumentException if the owner is null or blank, or the capacity is not positive
     */
    public LaundryBasket(String owner, double capacityKg) {
        if (owner == null || owner.isBlank() || capacityKg <= 0) {
            throw new IllegalArgumentException("Invalid owner or capacity");
        }

        this.owner = owner;
        this.capacityKg = capacityKg;
    }

    /**
     * @return the owner of the basket
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @return the capacity of the basket in kilograms
     */
    public double getCapacityKg() {
        return capacityKg;
    }

    /**
     * @return the total weight of the garments currently in the basket, in kilograms
     */
    public double getWeightKg() {
        return currentWeightKg;
    }

    /**
     * @return the number of garments currently in the basket
     */
    public int size() {
        return garments.size();
    }

    /**
     * Puts a garment on top of the basket.
     *
     * @param garment the garment to put in
     * @throws IllegalArgumentException  if the garment is null
     * @throws BasketOverloadedException if the total weight would exceed the capacity of the basket
     */
    public void add(G garment) {
        if (garment == null) {
            throw new IllegalArgumentException("Garment cannot be null");
        }

        double newGarmentWeight = garment.getWeightKg();
        double newCurrentWeight = currentWeightKg + newGarmentWeight;

        if (newCurrentWeight > capacityKg) {
            throw new BasketOverloadedException("Current weight cannot exceed the capacity");
        }

        garments.push(garment);
        currentWeightKg = newCurrentWeight;
    }

    private double calculateGarmentsTotalWeight(Collection<? extends G> garments) {
        double totalWeight = 0;

        for (G garment : garments) {
            totalWeight += garment.getWeightKg();
        }

        return  totalWeight;
    }

    /**
     * Puts all given garments on top of the basket in the iteration order of the collection.
     * Either all garments are put in, or none of them.
     *
     * @param garments the garments to put in
     * @throws IllegalArgumentException  if the collection is null or contains null
     * @throws BasketOverloadedException if the total weight would exceed the capacity of the basket
     */
    public void addAll(Collection<? extends G> garments) {
        if (garments == null) {
            throw new IllegalArgumentException("Garments cannot be null");
        }

        for (G garment : garments) {
            if (garment == null) {
                throw new IllegalArgumentException("Garment cannot be null");
            }
        }

        double totalGarmentsWeight = calculateGarmentsTotalWeight(garments);
        double newCurrentWeight = currentWeightKg + totalGarmentsWeight;

        if (newCurrentWeight > capacityKg) {
            throw new BasketOverloadedException("Current weight cannot exceed the capacity");
        }

        for (G garment : garments) {
            this.garments.push(garment);
        }

        currentWeightKg = newCurrentWeight;
    }

    /**
     * Moves all garments from this basket on top of the other one, preserving their stack order
     * (this basket's top becomes the other basket's top). After a move, this basket is empty.
     * Either all garments are moved, or none of them.
     *
     * @param other the basket to move the garments to
     * @return the number of moved garments
     * @throws IllegalArgumentException  if other is null or is the same object as this basket
     * @throws BasketOverloadedException if the total weight would exceed the capacity of the other basket
     */
    public int moveAllTo(LaundryBasket<? super G> other) {
        if (other == null || other == this) {
            throw new IllegalArgumentException("Invalid destination basket");
        }

        double newCurrentWeight = currentWeightKg + other.getWeightKg();

        if (newCurrentWeight > other.getCapacityKg()) {
            throw new BasketOverloadedException("Destination basket capacity exceeded");
        }

        int movedCount = garments.size();

        Deque<G> temp = new ArrayDeque<>();

        while (!garments.isEmpty()) {
            temp.push(garments.pop());
        }

        while (!temp.isEmpty()) {
            other.add(temp.pop());
        }

        currentWeightKg = 0;

        return movedCount;
    }

    /**
     * Takes out and returns the garment on top of the basket.
     *
     * @return the garment on top of the basket
     * @throws EmptyBasketException if the basket is empty
     */
    public G takeTop() {
        if (garments.isEmpty()) {
            throw new EmptyBasketException("Basket is empty");
        }

        G garment = garments.pop();
        currentWeightKg -= garment.getWeightKg();

        return garment;
    }

    /**
     * @return an iterator over the garments in the basket in LIFO order (from top to bottom)
     */
    @Override
    public Iterator<G> iterator() {
        return garments.iterator();
    }
}