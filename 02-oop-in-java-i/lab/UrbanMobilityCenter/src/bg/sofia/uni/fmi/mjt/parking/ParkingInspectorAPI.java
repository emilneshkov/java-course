package bg.sofia.uni.fmi.mjt.parking;

import bg.sofia.uni.fmi.mjt.parking.location.Location;
import bg.sofia.uni.fmi.mjt.parking.sanction.Sanction;
import bg.sofia.uni.fmi.mjt.parking.vehicle.Vehicle;

import java.time.LocalDateTime;

public interface ParkingInspectorAPI {

    /**
     * Inspects a parked vehicle at the given moment.
     *
     * @return a sanction if the location is in a zone and the parking there
     * is not paid at the given moment, or null if any argument is null,
     * the location is not in any zone, or the parking is paid
     */
    Sanction inspect(Vehicle vehicle, Location location, LocalDateTime moment);

}