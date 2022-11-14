package org.jetbrains.station;

import org.jetbrains.car.Car;
import org.jetbrains.car.ElectricCar;
import org.jetbrains.car.PetrolCar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class UnitStationTest {

    Car electricCar;
    Car petrolCar;
    double destinationLocation;

    @BeforeEach
    public void setUp() {
        electricCar = mock(ElectricCar.class);
        petrolCar = mock(PetrolCar.class);
        destinationLocation = 10;
    }

    @Test
    void testGasStation() {
        when(petrolCar.getLocation()).thenReturn(30.5);
        GasStation gasStation = StationsPool.getInstance().getClosestGasStation(petrolCar, destinationLocation);
        assertEquals(25, gasStation.getLocation());
    }

    @Test
    void testNotPetrolCar() {
        when(electricCar.getLocation()).thenReturn(30.5);
        assertThrows(IllegalArgumentException.class,
                () -> StationsPool.getInstance().getClosestGasStation(electricCar, destinationLocation));
    }

    @Test
    void testChargingStation() {
        when(electricCar.getLocation()).thenReturn(30.5);
        ChargingStation station = StationsPool.getInstance().getClosestChargingStation(electricCar, destinationLocation);
        assertEquals(35, station.getLocation());
    }

    @Test
    void testNotElectricCar() {
        when(petrolCar.getLocation()).thenReturn(30.5);
        assertThrows(IllegalArgumentException.class,
                () -> StationsPool.getInstance().getClosestChargingStation(petrolCar, destinationLocation));
    }

    @Test
    void testGasStationStress() {
        List<Integer> locations = Arrays.asList(10, 25, 45, 67, 77, 89, 97);
        for (int i = 0; i < 100; i++) {
            double location = ThreadLocalRandom.current().nextDouble(5, 100);
            when(petrolCar.getLocation()).thenReturn(location);
            GasStation gasStation = StationsPool.getInstance().getClosestGasStation(petrolCar, destinationLocation);
            double expectedLocation = 0;
            double minDestination = 100;
            for (Integer current : locations) {
                double destination = Math.abs(location - current);
                if (destination < minDestination) {
                    minDestination = destination;
                    expectedLocation = current;
                }
            }
            assertEquals(expectedLocation, gasStation.getLocation());
        }
    }

    @Test
    void testChargingStationStress() {
        List<Integer> locations = Arrays.asList(15, 35, 47, 59, 70, 86, 96);
        for (int i = 0; i < 100; i++) {
            double location = ThreadLocalRandom.current().nextDouble(5, 100);
            when(electricCar.getLocation()).thenReturn(location);
            ChargingStation station = StationsPool.getInstance().getClosestChargingStation(electricCar, destinationLocation);
            double expectedLocation = 0;
            double minDestination = 100;
            for (Integer current : locations) {
                double destination = Math.abs(location - current);
                if (destination < minDestination) {
                    minDestination = destination;
                    expectedLocation = current;
                }
            }
            assertEquals(expectedLocation, station.getLocation());
        }
    }

}
