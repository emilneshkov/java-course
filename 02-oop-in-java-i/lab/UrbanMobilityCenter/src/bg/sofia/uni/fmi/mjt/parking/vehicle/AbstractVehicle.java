package bg.sofia.uni.fmi.mjt.parking.vehicle;

public abstract class AbstractVehicle implements Vehicle {
    private final LicensePlate licensePlate;

    protected AbstractVehicle(LicensePlate licensePlate) {
        this.licensePlate = licensePlate;
    }

    @Override
    public LicensePlate getLicensePlate() {
        return  licensePlate;
    }

}
