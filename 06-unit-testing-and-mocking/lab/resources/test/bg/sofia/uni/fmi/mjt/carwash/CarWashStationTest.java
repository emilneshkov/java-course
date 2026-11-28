package bg.sofia.uni.fmi.mjt.carwash;

import bg.sofia.uni.fmi.mjt.carwash.controller.WashController;
import bg.sofia.uni.fmi.mjt.carwash.exception.WashUnavailableException;
import bg.sofia.uni.fmi.mjt.carwash.vehicle.Vehicle;
import bg.sofia.uni.fmi.mjt.carwash.vehicle.VehicleType;
import bg.sofia.uni.fmi.mjt.carwash.washpackage.BasicWashPackage;
import bg.sofia.uni.fmi.mjt.carwash.washpackage.WashPackage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarWashStationTest {


    @Mock
    private WashController controller;

    private CarWashStation station;
    private Vehicle car;
    private Vehicle van;
    private WashPackage washPackage;
    private WashRequest request;

    @BeforeEach
    void setUp() {
        station = new CarWashStation(2000, controller);

        car = new Vehicle("BT4855BH", VehicleType.CAR);
        van = new Vehicle("BT1234AB", VehicleType.VAN);

        washPackage = new BasicWashPackage();

        request = new WashRequest(car, washPackage.createPlan(car));
    }

    @Test
    void testConstructorWithNegativeCredit() {
        assertThrows(IllegalArgumentException.class,
                () -> new CarWashStation(-1, controller),
                "Negative credit should be rejected");
    }

    @Test
    void testConstructorWithNullController() {
        assertThrows(IllegalArgumentException.class,
                () -> new CarWashStation(2000, null),
                "Null controller should be rejected");
    }

    @Test
    void testConstructorInitializesState() {
        assertEquals(2000, station.getCreditCents(), "Incorrect initial credit");

        assertTrue(station.getAcceptedWashes().isEmpty(), "History should be empty");

        verifyNoInteractions(controller);
    }

    @Test
    void testConstructorWithZeroCredit() {
        CarWashStation emptyStation = new CarWashStation(0, controller);

        assertEquals(0, emptyStation.getCreditCents(), "Zero credit should be allowed");
    }

    @Test
    void testAddCreditWithNegativeAmount() {
        assertThrows(IllegalArgumentException.class,
                () -> station.addCredit(-100),
                "Negative amount should be rejected");

        assertEquals(2000, station.getCreditCents(), "Credit should remain unchanged");

        verifyNoInteractions(controller);
    }

    @Test
    void testAddCreditWithZeroAmount() {
        assertThrows(IllegalArgumentException.class,
                () -> station.addCredit(0),
                "Zero amount should be rejected");

        assertEquals(2000, station.getCreditCents(), "Credit should remain unchanged");

        verifyNoInteractions(controller);
    }

    @Test
    void testAddCreditIncreasesBalanceWithoutSideEffects() {
        station.addCredit(500);

        assertEquals(2500, station.getCreditCents(), "Incorrect credit");

        assertTrue(station.getAcceptedWashes().isEmpty(), "History should remain empty");

        verifyNoInteractions(controller);
    }

    @Test
    void testRequestWashWithNullVehicle() {
        assertThrows(IllegalArgumentException.class,
                () -> station.requestWash(null, washPackage),
                "Null vehicle should be rejected");

        verifyNoInteractions(controller);
    }

    @Test
    void testRequestWashWithNullWashPackage() {
        assertThrows(IllegalArgumentException.class,
                () -> station.requestWash(car, null),
                "Null package should be rejected");

        verifyNoInteractions(controller);
    }

    @Test
    void testRequestWashWithUnsupportedVehicle() {
        assertFalse(station.requestWash(van, washPackage),
                "Unsupported vehicle should be rejected");

        assertEquals(2000, station.getCreditCents(), "Credit should remain unchanged");

        assertTrue(station.getAcceptedWashes().isEmpty(), "History should remain empty");

        verifyNoInteractions(controller);
    }

    @Test
    void testRequestWashWithInsufficientCredit() {
        station = new CarWashStation(599, controller);

        assertFalse(station.requestWash(car, washPackage),
                "Request should be rejected");

        assertEquals(599, station.getCreditCents(), "Credit should remain unchanged");

        assertTrue(station.getAcceptedWashes().isEmpty(), "History should remain empty");

        verifyNoInteractions(controller);
    }

    @Test
    void testRequestWashAcceptedByController() {
        when(controller.submit(request)).thenReturn(true);

        assertTrue(station.requestWash(car, washPackage),
                "Request should be accepted");

        assertEquals(1400, station.getCreditCents(), "Incorrect remaining credit");

        assertEquals(1, station.getAcceptedWashes().size(), "Incorrect history size");

        assertEquals(request, station.getAcceptedWashes().get(0),
                "Incorrect recorded request");

        verify(controller, times(1)).submit(request);
        verifyNoMoreInteractions(controller);
    }

    @Test
    void testRequestWashRejectedByController() {
        when(controller.submit(request)).thenReturn(false);

        assertFalse(station.requestWash(car, washPackage),
                "Request should be rejected");

        assertEquals(2000, station.getCreditCents(), "Credit should remain unchanged");

        assertTrue(station.getAcceptedWashes().isEmpty(), "History should remain empty");

        verify(controller, times(1)).submit(request);
        verifyNoMoreInteractions(controller);
    }

    @Test
    void testRequestWashWithExactlyEnoughCredit() {
        station = new CarWashStation(600, controller);

        when(controller.submit(request)).thenReturn(true);

        assertTrue(station.requestWash(car, washPackage),
                "Request should be accepted");

        assertEquals(0, station.getCreditCents(), "Credit should be exhausted");

        assertEquals(1, station.getAcceptedWashes().size(), "Request should be recorded");

        verify(controller).submit(request);
    }

    @Test
    void testRequestWashWhenControllerThrowsException() {
        WashUnavailableException exception =
                new WashUnavailableException("Controller unavailable");

        when(controller.submit(request))
                .thenReturn(true)
                .thenThrow(exception);

        assertTrue(station.requestWash(car, washPackage),
                "First request should be accepted");

        int credit = station.getCreditCents();
        List<WashRequest> history = station.getAcceptedWashes();

        WashUnavailableException thrown = assertThrows(
                WashUnavailableException.class,
                () -> station.requestWash(car, washPackage),
                "Exception should be propagated");

        assertSame(exception, thrown, "Original exception should be preserved");

        assertEquals(credit, station.getCreditCents(), "Credit should remain unchanged");

        assertEquals(history, station.getAcceptedWashes(), "History should remain unchanged");

        verify(controller, times(2)).submit(request);
    }

    @Test
    void testRequestWashRecordsRepeatedRequestsSeparately() {
        when(controller.submit(request)).thenReturn(true);

        assertTrue(station.requestWash(car, washPackage),
                "First request should be accepted");

        assertTrue(station.requestWash(car, washPackage),
                "Second request should be accepted");

        assertEquals(800, station.getCreditCents(), "Incorrect remaining credit");

        assertEquals(2, station.getAcceptedWashes().size(), "Both requests should be recorded");

        assertEquals(request, station.getAcceptedWashes().get(0), "Incorrect first request");

        assertEquals(request, station.getAcceptedWashes().get(1), "Incorrect second request");

        verify(controller, times(2)).submit(request);
    }

    @Test
    void testGetAcceptedWashesReturnsUnmodifiableList() {
        when(controller.submit(request)).thenReturn(true);

        station.requestWash(car, washPackage);

        List<WashRequest> history = station.getAcceptedWashes();

        assertThrows(UnsupportedOperationException.class,
                () -> history.add(request),
                "Adding requests should be forbidden");

        assertThrows(UnsupportedOperationException.class,
                () -> history.remove(0),
                "Removing requests should be forbidden");

        assertThrows(UnsupportedOperationException.class,
                () -> history.set(0, request),
                "Replacing requests should be forbidden");
    }

    @Test
    void testGetAcceptedWashesReturnsOrderedSnapshot() {
        Vehicle secondCar = new Vehicle("BT5678AB", VehicleType.CAR);

        WashRequest secondRequest =
                new WashRequest(secondCar, washPackage.createPlan(secondCar));

        when(controller.submit(request)).thenReturn(true);
        when(controller.submit(secondRequest)).thenReturn(true);

        station.requestWash(car, washPackage);

        List<WashRequest> snapshot = station.getAcceptedWashes();

        station.requestWash(secondCar, washPackage);

        List<WashRequest> expectedHistory = new ArrayList<>();
        expectedHistory.add(request);
        expectedHistory.add(secondRequest);

        assertEquals(1, snapshot.size(), "Snapshot should remain unchanged");

        assertEquals(request, snapshot.get(0), "Incorrect snapshot request");

        assertEquals(expectedHistory, station.getAcceptedWashes(), "Incorrect submission order");
    }


}
