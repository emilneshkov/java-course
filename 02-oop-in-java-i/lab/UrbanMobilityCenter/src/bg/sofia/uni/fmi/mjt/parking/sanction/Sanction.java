package bg.sofia.uni.fmi.mjt.parking.sanction;

import bg.sofia.uni.fmi.mjt.parking.vehicle.Vehicle;
import bg.sofia.uni.fmi.mjt.parking.zone.ParkingZone;

/**
 * A sanction for unpaid parking.
 */
public interface Sanction {

    /**
     * @return the vehicle the sanction is imposed on
     */
    Vehicle getVehicle();

    /**
     * @return the zone in which the sanction is imposed
     */
    ParkingZone getZone();

    /**
     * @return the amount of the sanction in euro:
     * the base amount of the sanction plus the surcharge of the vehicle
     */
    int getAmount();

}