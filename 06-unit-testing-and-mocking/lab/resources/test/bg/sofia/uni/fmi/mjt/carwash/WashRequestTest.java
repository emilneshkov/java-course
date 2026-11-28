package bg.sofia.uni.fmi.mjt.carwash;

import bg.sofia.uni.fmi.mjt.carwash.plan.WashPlan;
import bg.sofia.uni.fmi.mjt.carwash.plan.WashStep;
import bg.sofia.uni.fmi.mjt.carwash.vehicle.Vehicle;
import bg.sofia.uni.fmi.mjt.carwash.vehicle.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WashRequestTest {

    private Vehicle vehicle;
    private WashPlan plan;

    @BeforeEach
    void setUp() {
        vehicle = new Vehicle("BT4855BH", VehicleType.CAR);

        List<WashStep> steps = new ArrayList<>();
        steps.add(WashStep.SHAMPOO);
        steps.add(WashStep.RINSE);

        plan = new WashPlan("Basic", 600, steps);
    }

    @Test
    void testConstructorWithNullVehicle() {
        assertThrows(IllegalArgumentException.class,
                () -> new WashRequest(null, plan),
                "Null vehicle should be rejected");
    }

    @Test
    void testConstructorWithNullPlan() {
        assertThrows(IllegalArgumentException.class,
                () -> new WashRequest(vehicle, null),
                "Null plan should be rejected");
    }

    @Test
    void testRequestRetainsSuppliedVehicleAndPlan() {
        WashRequest request = new WashRequest(vehicle, plan);

        assertSame(vehicle, request.vehicle(), "Vehicle should be retained");

        assertSame(plan, request.plan(), "Plan should be retained");
    }
}
