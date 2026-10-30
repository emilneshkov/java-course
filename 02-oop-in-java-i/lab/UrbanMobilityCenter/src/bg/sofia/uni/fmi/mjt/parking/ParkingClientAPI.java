package bg.sofia.uni.fmi.mjt.parking;

import java.time.LocalDateTime;

public interface ParkingClientAPI {

    /**
     * Pays one hour of parking in the zone with the given short number,
     * starting at the given moment.
     *
     * @param smsNumber the short number, e.g. "1302"
     * @param plate     the license plate, as typed by the driver
     * @param moment    the moment the SMS is sent
     * @return true if the payment is accepted, and false if any argument is null,
     * the short number does not belong to any zone or the plate is not valid
     */
    boolean sendSms(String smsNumber, String plate, LocalDateTime moment);

}