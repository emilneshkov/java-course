package bg.sofia.uni.fmi.mjt.parking.vehicle;

/**
 * A vehicle that can be parked in a paid zone.
 */
public interface Vehicle {

    /**
     * @return the license plate of the vehicle
     */
    LicensePlate getLicensePlate();

    /**
     * @return the amount in euro added to every sanction imposed on the vehicle
     */
    int getSurcharge();

}