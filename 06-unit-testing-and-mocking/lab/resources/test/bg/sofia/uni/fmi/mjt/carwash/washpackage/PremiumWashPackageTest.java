package bg.sofia.uni.fmi.mjt.carwash.washpackage;

import bg.sofia.uni.fmi.mjt.carwash.exception.UnsupportedVehicleException;
import bg.sofia.uni.fmi.mjt.carwash.plan.WashPlan;
import bg.sofia.uni.fmi.mjt.carwash.plan.WashStep;
import bg.sofia.uni.fmi.mjt.carwash.vehicle.Vehicle;
import bg.sofia.uni.fmi.mjt.carwash.vehicle.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PremiumWashPackageTest {
    private WashPackage washPackage;
    private Vehicle car;
    private Vehicle van;

    @BeforeEach
    void setUp() {
        washPackage = new PremiumWashPackage();
        car = new Vehicle("BT4855BH", VehicleType.CAR);
        van = new Vehicle("BT1234AB", VehicleType.VAN);
    }

    @Test
    void testCreatePlanWithNullVehicle() {
        assertThrows(IllegalArgumentException.class,
                () -> washPackage.createPlan(null),
                "Null vehicle should be rejected");
    }

    @Test
    void testCreatePlanWithUnsupportedVehicle() {
        assertThrows(UnsupportedVehicleException.class,
                () -> washPackage.createPlan(van),
                "Van should be rejected");
    }

    @Test
    void testCreatePlanReturnsIndependentPlans() {
        WashPlan firstPlan = washPackage.createPlan(car);
        WashPlan secondPlan = washPackage.createPlan(car);

        assertNotSame(firstPlan, secondPlan, "Plans should be different instances");

        assertEquals(firstPlan, secondPlan, "Plans should have equal values");
    }

    @Test
    void testCreatePlanReturnsCorrectPlan() {
        List<WashStep> expectedSteps = new ArrayList<>();
        expectedSteps.add(WashStep.PREWASH);
        expectedSteps.add(WashStep.SHAMPOO);
        expectedSteps.add(WashStep.RINSE);
        expectedSteps.add(WashStep.WAX);
        expectedSteps.add(WashStep.DRY);

        WashPlan plan = washPackage.createPlan(car);

        assertNotNull(plan, "Plan should not be null");
        assertEquals("Premium", plan.packageName(), "Incorrect package name");
        assertEquals(1000, plan.priceCents(), "Incorrect price");
        assertEquals(expectedSteps, plan.steps(), "Incorrect wash steps");
    }

}
