package bg.sofia.uni.fmi.mjt.parking;

import bg.sofia.uni.fmi.mjt.parking.location.Location;
import bg.sofia.uni.fmi.mjt.parking.sanction.AbstractSanction;
import bg.sofia.uni.fmi.mjt.parking.sanction.Sanction;
import bg.sofia.uni.fmi.mjt.parking.sanction.TowAway;
import bg.sofia.uni.fmi.mjt.parking.sanction.WheelClamp;
import bg.sofia.uni.fmi.mjt.parking.vehicle.LicensePlate;
import bg.sofia.uni.fmi.mjt.parking.vehicle.Vehicle;
import bg.sofia.uni.fmi.mjt.parking.zone.AbstractZone;
import bg.sofia.uni.fmi.mjt.parking.zone.BlueZone;
import bg.sofia.uni.fmi.mjt.parking.zone.GreenZone;
import bg.sofia.uni.fmi.mjt.parking.zone.ParkingZone;

import java.time.LocalDateTime;

public class UrbanMobilityCenter implements ParkingClientAPI,ParkingInspectorAPI {
    private  final BlueZone blueZone;
    private  final GreenZone greenZone;

    private ParkingPayment[] payments = new ParkingPayment[10];
    private int paymentCount = 0;

    public UrbanMobilityCenter(BlueZone blueZone, GreenZone greenZone)
    {
        this.blueZone=blueZone;
        this.greenZone=greenZone;
    };


    @Override
    public boolean sendSms(String smsNumber, String plate, LocalDateTime moment){
        if(smsNumber == null || plate == null || moment == null){
            return false;
        }

        if(!smsNumber.equals("1302") && !smsNumber.equals("1303")){
            return false;
        }

        LicensePlate licensePlate =  LicensePlate.parse(plate);
        if(licensePlate == null){
            return  false;
        }

        AbstractZone parkingZone;
        if(smsNumber.equals("1302")){
           parkingZone = blueZone;
        }
        else {
            parkingZone = greenZone;
        }

        ParkingPayment parkingPayment = new ParkingPayment(licensePlate,parkingZone,moment);

        if (paymentCount == payments.length) {
            resizePayments();
        }

        payments[paymentCount++] = parkingPayment;

        return true;
    }

    @Override
    public Sanction inspect(Vehicle vehicle, Location location, LocalDateTime moment){
        if(vehicle == null || location == null || moment == null){
            return null;
        }

        ParkingZone zone;

        if (blueZone.contains(location)) {
            zone = blueZone;
        } else if (greenZone.contains(location)) {
            zone = greenZone;
        } else {
            return null;
        }

        for (int i = 0; i < paymentCount; i++) {
            ParkingPayment payment = payments[i];

            boolean samePlate = payment.getLicensePlate().equals(vehicle.getLicensePlate());

            boolean sameZone = payment.getZone() == zone;

            LocalDateTime startTime = payment.getStartTime();
            LocalDateTime endTime = startTime.plusHours(1);

            boolean validTime = !moment.isBefore(startTime) && moment.isBefore(endTime);

            if (samePlate && sameZone && validTime) {
                return null;
            }
        }
        Sanction sanction;

        if(zone instanceof BlueZone){
            sanction = new TowAway(vehicle,zone);
        }
        else{
            sanction = new WheelClamp(vehicle,zone);
        }

        return sanction;
    }

    private void resizePayments() {
        ParkingPayment[] resized = new ParkingPayment[payments.length * 2];

        for (int i = 0; i < payments.length; i++) {
            resized[i] = payments[i];
        }

        payments = resized;
    }
}
