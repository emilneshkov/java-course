package bg.sofia.uni.fmi.mjt.parking.sanction;

import bg.sofia.uni.fmi.mjt.parking.vehicle.Vehicle;
import bg.sofia.uni.fmi.mjt.parking.zone.ParkingZone;

public abstract class AbstractSanction implements Sanction{
    private final Vehicle vehicle;
    private final ParkingZone zone;

    protected AbstractSanction(Vehicle vehicle, ParkingZone zone) {
        this.vehicle = vehicle;
        this.zone = zone;
    }

    @Override
    public Vehicle getVehicle() {
        return vehicle;
    }

    @Override
    public ParkingZone getZone() {
        return zone;
    }

}
