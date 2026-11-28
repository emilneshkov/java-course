package bg.sofia.uni.fmi.mjt.carwash;

import bg.sofia.uni.fmi.mjt.carwash.controller.WashController;
import bg.sofia.uni.fmi.mjt.carwash.exception.UnsupportedVehicleException;
import bg.sofia.uni.fmi.mjt.carwash.plan.WashPlan;
import bg.sofia.uni.fmi.mjt.carwash.vehicle.Vehicle;
import bg.sofia.uni.fmi.mjt.carwash.washpackage.WashPackage;

import java.util.ArrayList;
import java.util.List;

public class CarWashStation {

    private final WashController controller;
    private final List<WashRequest> acceptedWashes;

    private int creditCents;

    public CarWashStation(int initialCreditCents, WashController controller) {
        if (initialCreditCents < 0) {
            throw new IllegalArgumentException("Initial credit must not be negative");
        }

        if (controller == null) {
            throw new IllegalArgumentException("Controller must not be null");
        }

        this.creditCents = initialCreditCents;
        this.controller = controller;
        this.acceptedWashes = new ArrayList<>();
    }

    public void addCredit(int amountCents) {
        if (amountCents <= 0) {
            throw new IllegalArgumentException("Added credit must be positive");
        }

        creditCents += amountCents;
    }

    public boolean requestWash(Vehicle vehicle, WashPackage washPackage) {
        if (vehicle == null || washPackage == null) {
            throw new IllegalArgumentException("Vehicle and wash package must not be null");
        }

        WashPlan plan;
        try {
            plan = createWashPlan(vehicle, washPackage);
        } catch (UnsupportedVehicleException exception) {
            return false;
        }

        if (!hasSufficientCredit(plan)) {
            return false;
        }

        WashRequest request = new WashRequest(vehicle, plan);

        // BUG 5: Failed requests incorrectly increase credit.
        if (!controller.submit(request)) {
            return false;
        }

        chargeAndRecordWash(plan, request);
        return true;
    }

    private WashPlan createWashPlan(Vehicle vehicle, WashPackage washPackage) {
        return washPackage.createPlan(vehicle);
    }

    private void chargeAndRecordWash(WashPlan plan, WashRequest request) {
        creditCents -= plan.priceCents();

        // BUG 6: Duplicate accepted requests are ignored.
        acceptedWashes.add(request);
    }

    private boolean hasSufficientCredit(WashPlan plan) {
        return creditCents >= plan.priceCents();
    }

    public int getCreditCents() {
        return creditCents;
    }

    public List<WashRequest> getAcceptedWashes() {
        return List.copyOf(acceptedWashes);
    }
}