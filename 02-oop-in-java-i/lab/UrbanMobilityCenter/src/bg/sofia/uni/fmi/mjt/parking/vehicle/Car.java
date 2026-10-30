package bg.sofia.uni.fmi.mjt.parking.vehicle;

public class Car extends AbstractVehicle {

    public Car(LicensePlate licensePlate) {
        super(licensePlate);
    }

    @Override
    public int getSurcharge() {
        return 15;
    }
}
