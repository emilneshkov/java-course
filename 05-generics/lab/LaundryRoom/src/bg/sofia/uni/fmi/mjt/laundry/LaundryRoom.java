package bg.sofia.uni.fmi.mjt.laundry;

import bg.sofia.uni.fmi.mjt.laundry.basket.LaundryBasket;
import bg.sofia.uni.fmi.mjt.laundry.exception.AlreadyQueuedException;
import bg.sofia.uni.fmi.mjt.laundry.exception.EmptyQueueException;
import bg.sofia.uni.fmi.mjt.laundry.garment.Garment;
import bg.sofia.uni.fmi.mjt.laundry.program.WashProgram;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class LaundryRoom implements LaundryRoomAPI {
    private final Queue<WashJob<?>> queue = new ArrayDeque<>();
    private final Map<String, Integer> tokensSpent = new HashMap<>();

    @Override
    public <G extends Garment> WashJob<G> scheduleWash(LaundryBasket<G> basket, WashProgram<? super G> program) {
        if (basket == null || program == null || basket.size() == 0) {
            throw new IllegalArgumentException("Invalid arguments");
        }

        for (WashJob<?> job : queue) {
            if (job.basket() == basket) {
                throw new AlreadyQueuedException("Basket is already queued");
            }
        }

        WashJob<G> job = new WashJob<>(basket, program);
        queue.add(job);

        return job;
    }

    @Override
    public boolean cancelWash(LaundryBasket<?> basket) {
        if (basket == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }

        for (WashJob<?> job : queue) {
            if (job.basket() == basket) {
                queue.remove(job);
                return true;
            }
        }

        return false;
    }

    @Override
    public WashJob<?> runNext() {
        if (queue.isEmpty()) {
            throw new EmptyQueueException("Queue is empty");
        }

        WashJob<?> job = queue.poll();
        job.wash();

        String owner = job.basket().getOwner();

        if (tokensSpent.containsKey(owner)) {
            int tokens = tokensSpent.get(owner);

            tokensSpent.put(owner, tokens + 1);
        } else {
            tokensSpent.put(owner, 1);
        }

        return job;
    }

    @Override
    public List<WashJob<?>> getScheduledWashes() {
        return Collections.unmodifiableList(new ArrayList<>(queue));
    }

    @Override
    public int getTokensSpent(String owner) {
        if (owner == null || owner.isBlank()) {
            throw new IllegalArgumentException("Invalid owner");
        }

        if (tokensSpent.containsKey(owner)) {
            return tokensSpent.get(owner);
        }

        return 0;
    }
}
