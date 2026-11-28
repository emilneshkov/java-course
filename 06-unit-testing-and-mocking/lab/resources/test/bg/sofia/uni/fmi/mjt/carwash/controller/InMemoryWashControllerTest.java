package bg.sofia.uni.fmi.mjt.carwash.controller;

import bg.sofia.uni.fmi.mjt.carwash.WashRequest;
import bg.sofia.uni.fmi.mjt.carwash.exception.WashUnavailableException;
import bg.sofia.uni.fmi.mjt.carwash.plan.WashPlan;
import bg.sofia.uni.fmi.mjt.carwash.plan.WashStep;
import bg.sofia.uni.fmi.mjt.carwash.vehicle.Vehicle;
import bg.sofia.uni.fmi.mjt.carwash.vehicle.VehicleType;
import bg.sofia.uni.fmi.mjt.carwash.washpackage.PremiumWashPackage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InMemoryWashControllerTest {

    private InMemoryWashController controller;
    private WashSupplies initialSupplies;
    private Vehicle vehicle;
    private WashRequest request;

    @BeforeEach
    void setUp() {
        initialSupplies = new WashSupplies(100, 100, 100);
        controller = new InMemoryWashController(initialSupplies);

        vehicle = new Vehicle("BT4855BH", VehicleType.CAR);

        WashPlan plan = new PremiumWashPackage().createPlan(vehicle);
        request = new WashRequest(vehicle, plan);
    }

    @Test
    void testConstructorWithNullSupplies() {
        assertThrows(IllegalArgumentException.class,
                () -> new InMemoryWashController(null),
                "Null supplies should be rejected");
    }

    @Test
    void testConstructorInitializesState() {
        assertEquals(initialSupplies, controller.getSupplies(), "Incorrect initial supplies");

        assertTrue(controller.getAcceptedWashes().isEmpty(), "History should be empty");
    }

    @Test
    void testSubmitWithNullRequest() {
        assertThrows(IllegalArgumentException.class,
                () -> controller.submit(null),
                "Null request should be rejected");

        assertEquals(initialSupplies, controller.getSupplies(), "Supplies should remain unchanged");

        assertTrue(controller.getAcceptedWashes().isEmpty(), "History should remain empty");
    }

    @Test
    void testSubmitValidatesRequestBeforeOperationalState() {
        controller.setOperational(false);

        assertThrows(IllegalArgumentException.class,
                () -> controller.submit(null),
                "Request validation should come first");
    }

    @Test
    void testSubmitWhenControllerIsNotOperational() {
        controller.setOperational(false);

        assertThrows(WashUnavailableException.class,
                () -> controller.submit(request),
                "Unavailable controller should throw");

        assertEquals(initialSupplies, controller.getSupplies(), "Supplies should remain unchanged");

        assertTrue(controller.getAcceptedWashes().isEmpty(), "History should remain empty");
    }

    @Test
    void testSubmitConsumesSuppliesAndRecordsRequest() {
        assertTrue(controller.submit(request), "Request should be accepted");

        WashSupplies expectedSupplies = new WashSupplies(70, 80, 90);

        assertEquals(expectedSupplies, controller.getSupplies(), "Incorrect remaining supplies");

        assertEquals(1, controller.getAcceptedWashes().size(), "Incorrect history size");

        assertEquals(request, controller.getAcceptedWashes().get(0), "Incorrect recorded request");
    }

    @Test
    void testSubmitWithExactlyEnoughSupplies() {
        controller = new InMemoryWashController(
                new WashSupplies(30, 20, 10));

        assertTrue(controller.submit(request), "Request should be accepted");

        assertEquals(new WashSupplies(0, 0, 0),
                controller.getSupplies(),
                "Supplies should be exhausted");

        assertEquals(1, controller.getAcceptedWashes().size(), "Request should be recorded");
    }

    @Test
    void testSubmitWithInsufficientWater() {
        WashSupplies supplies = new WashSupplies(29, 100, 100);
        controller = new InMemoryWashController(supplies);

        assertFalse(controller.submit(request), "Request should be rejected");

        assertEquals(supplies, controller.getSupplies(), "Supplies should remain unchanged");

        assertTrue(controller.getAcceptedWashes().isEmpty(), "History should remain empty");
    }

    @Test
    void testSubmitWithInsufficientShampoo() {
        WashSupplies supplies = new WashSupplies(100, 19, 100);
        controller = new InMemoryWashController(supplies);

        assertFalse(controller.submit(request), "Request should be rejected");

        assertEquals(supplies, controller.getSupplies(), "Supplies should remain unchanged");

        assertTrue(controller.getAcceptedWashes().isEmpty(), "History should remain empty");
    }

    @Test
    void testSubmitWithInsufficientWax() {
        WashSupplies supplies = new WashSupplies(100, 100, 9);
        controller = new InMemoryWashController(supplies);

        assertFalse(controller.submit(request), "Request should be rejected");

        assertEquals(supplies, controller.getSupplies(), "Supplies should remain unchanged");

        assertTrue(controller.getAcceptedWashes().isEmpty(), "History should remain empty");
    }

    @Test
    void testSubmitConsumesSuppliesForRepeatedSteps() {
        List<WashStep> steps = new ArrayList<>();
        steps.add(WashStep.SHAMPOO);
        steps.add(WashStep.SHAMPOO);

        WashPlan plan = new WashPlan("Custom", 600, steps);
        WashRequest repeatedRequest = new WashRequest(vehicle, plan);

        assertTrue(controller.submit(repeatedRequest), "Request should be accepted");

        assertEquals(new WashSupplies(90, 60, 100),
                controller.getSupplies(),
                "Repeated steps should consume supplies");
    }

    @Test
    void testSubmitRecordsRepeatedRequestsSeparately() {
        assertTrue(controller.submit(request), "First request should be accepted");

        assertTrue(controller.submit(request), "Second request should be accepted");

        assertEquals(2, controller.getAcceptedWashes().size(), "Both requests should be recorded");

        assertEquals(request, controller.getAcceptedWashes().get(0), "Incorrect first request");

        assertEquals(request, controller.getAcceptedWashes().get(1), "Incorrect second request");

        assertEquals(new WashSupplies(40, 60, 80),
                controller.getSupplies(),
                "Incorrect remaining supplies");
    }

    @Test
    void testRefillWithNullSupplies() {
        assertThrows(IllegalArgumentException.class,
                () -> controller.refill(null),
                "Null supplies should be rejected");

        assertEquals(initialSupplies, controller.getSupplies(), "Supplies should remain unchanged");
    }

    @Test
    void testRefillAddsSuppliesWithoutSideEffects() {
        controller.submit(request);
        controller.setOperational(false);

        List<WashRequest> history = controller.getAcceptedWashes();

        controller.refill(new WashSupplies(20, 30, 40));

        assertEquals(new WashSupplies(90, 110, 130),
                controller.getSupplies(),
                "Incorrect supplies");

        assertEquals(history, controller.getAcceptedWashes(), "History should remain unchanged");

        assertThrows(WashUnavailableException.class,
                () -> controller.submit(request),
                "Controller should remain unavailable");
    }

    @Test
    void testSetOperationalChangesOnlyAvailability() {
        controller.submit(request);

        WashSupplies supplies = controller.getSupplies();
        List<WashRequest> history = controller.getAcceptedWashes();

        controller.setOperational(false);

        assertEquals(supplies, controller.getSupplies(), "Supplies should remain unchanged");
        assertEquals(history, controller.getAcceptedWashes(), "History should remain unchanged");

        controller.setOperational(true);

        assertEquals(supplies, controller.getSupplies(), "Supplies should remain unchanged");
        assertEquals(history, controller.getAcceptedWashes(), "History should remain unchanged");

        assertTrue(controller.submit(request), "Controller should be operational");
    }

    @Test
    void testGetSuppliesReturnsSnapshot() {
        WashSupplies snapshot = controller.getSupplies();

        controller.submit(request);

        assertEquals(initialSupplies, snapshot, "Snapshot should remain unchanged");
    }

    @Test
    void testGetAcceptedWashesReturnsUnmodifiableList() {
        controller.submit(request);

        List<WashRequest> history = controller.getAcceptedWashes();

        assertThrows(UnsupportedOperationException.class,
                () -> history.add(request),
                "Adding requests should be forbidden");

        assertThrows(UnsupportedOperationException.class,
                () -> history.remove(0),
                "Removing requests should be forbidden");

        assertThrows(UnsupportedOperationException.class,
                () -> history.set(0, request),
                "Replacing requests should be forbidden");
    }

    @Test
    void testGetAcceptedWashesReturnsOrderedSnapshot() {
        Vehicle secondVehicle = new Vehicle("CB5678AB", VehicleType.CAR);
        WashRequest secondRequest = new WashRequest(secondVehicle, request.plan());

        controller.submit(request);

        List<WashRequest> snapshot = controller.getAcceptedWashes();

        controller.submit(secondRequest);

        List<WashRequest> expectedHistory = new ArrayList<>();
        expectedHistory.add(request);
        expectedHistory.add(secondRequest);

        assertEquals(1, snapshot.size(), "Snapshot should remain unchanged");
        assertEquals(request, snapshot.get(0), "Incorrect snapshot request");

        assertEquals(expectedHistory, controller.getAcceptedWashes(), "Incorrect submission order");
    }

    @Test
    void testSubmitWithInsufficientSuppliesForRepeatedSteps() {
        WashSupplies supplies = new WashSupplies(100, 30, 100);
        controller = new InMemoryWashController(supplies);

        List<WashStep> steps = new ArrayList<>();
        steps.add(WashStep.SHAMPOO);
        steps.add(WashStep.SHAMPOO);

        WashPlan plan = new WashPlan("Custom", 600, steps);
        WashRequest repeatedRequest = new WashRequest(vehicle, plan);

        assertFalse(controller.submit(repeatedRequest), "Request should be rejected");

        assertEquals(supplies, controller.getSupplies(), "Supplies should remain unchanged");

        assertTrue(controller.getAcceptedWashes().isEmpty(), "History should remain empty");
    }


}
