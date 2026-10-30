package bg.sofia.uni.fmi.mjt.parking.zone;

import bg.sofia.uni.fmi.mjt.parking.location.Location;

/**
 * A paid parking zone.
 */
public interface ParkingZone {

    /**
     * @return the name of the zone, e.g. "Lozenets"
     */
    String getName();

    /**
     * @return the short number for SMS payment of parking in the zone
     */
    String getSmsNumber();

    /**
     * Checks whether the given location is in the zone.
     *
     * @param location the location to check
     * @return true if the location is in the zone and false otherwise.
     * If the location is null, returns false.
     */
    boolean contains(Location location);

}