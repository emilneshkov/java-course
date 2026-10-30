package bg.sofia.uni.fmi.mjt.parking;

import bg.sofia.uni.fmi.mjt.parking.vehicle.LicensePlate;
import bg.sofia.uni.fmi.mjt.parking.zone.ParkingZone;

import java.time.LocalDateTime;

class ParkingPayment {
    private final LicensePlate licensePlate;
    private final ParkingZone zone;
    private final LocalDateTime startTime;

    ParkingPayment(LicensePlate licensePlate, ParkingZone zone, LocalDateTime startTime) {
        this.licensePlate = licensePlate;
        this.zone = zone;
        this.startTime = startTime;
    }

    LicensePlate getLicensePlate() {
        return licensePlate;
    }

    ParkingZone getZone() {
        return zone;
    }

    LocalDateTime getStartTime() {
        return startTime;
    }
}
