package bg.sofia.uni.fmi.mjt.carwash.vehicle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VehicleTest {

    @Test
    void testVehicleWithNullRegistrationNumber() {
        assertThrows(IllegalArgumentException.class, () -> new Vehicle(null, VehicleType.CAR), "Null registration number should be rejected");
    }

    @Test
    void testVehicleWithEmptyRegistrationNumber() {
        assertThrows(IllegalArgumentException.class, () -> new Vehicle("", VehicleType.CAR), "Empty registration number should be rejected");
    }

    @Test
    void testVehicleWithBlankRegistrationNumber() {
        assertThrows(IllegalArgumentException.class, () -> new Vehicle("   ", VehicleType.CAR), "Blank registration number should be rejected");
    }

    @Test
    void testVehicleWithNullType() {
        assertThrows(IllegalArgumentException.class, () -> new Vehicle("BT4855BH", null), "Null vehicle type should be rejected");
    }

    @Test
    void testRegistrationNumberRemainsUnchanged() {
        String registrationNumber = " cb 1234 ab ";

        Vehicle vehicle = new Vehicle(registrationNumber, VehicleType.CAR);

        assertEquals(registrationNumber, vehicle.registrationNumber(),
                "Registration number should remain unchanged");
    }

}
