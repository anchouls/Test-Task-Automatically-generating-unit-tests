package org.jetbrains.car;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

public class UnitCarTest {

    double location;
    double energyUsageRate;
    Car car;

    @BeforeEach
    void setUp() {
        location = 30.5;
        energyUsageRate = 2;
        car = new PetrolCar(location, energyUsageRate);
    }

    @Test
    void testInitialCar() {
        double destination = ThreadLocalRandom.current().nextDouble(0, 50);

        assertFalse(car.needsEnergy(destination));
        assertEquals(location, car.getLocation());
        assertEquals(100, car.getEnergyValue());
    }

    @Test
    void testNeedsEnergy() {
        double destination = (car.getEnergyValue() - car.energyThreshold + 1) / energyUsageRate + location;

        assertTrue(car.needsEnergy(destination));
        assertEquals(location, car.getLocation());
        assertEquals(100, car.getEnergyValue());
    }

    @Test
    void testNegativeEnergyUsageRate() {
        energyUsageRate = -2;
        assertThrows(IllegalArgumentException.class, () -> new PetrolCar(location, energyUsageRate));
    }

    @Test
    void testDriveTo() {
        double destination = ThreadLocalRandom.current().nextDouble(0, 50);
        double distance = Math.abs(destination - location);
        double energy = car.getEnergyValue() - distance * energyUsageRate;

        car.driveTo(destination);
        assertEquals(destination, car.getLocation());
        assertEquals(energy, car.getEnergyValue());
    }

    @Test
    void testRefuel() {
        double destination = ThreadLocalRandom.current().nextDouble(0, 50);
        assertEquals(100, car.getEnergyValue());

        car.driveTo(destination);
        assertNotEquals(100, car.getEnergyValue());

        car.refuel();
        assertEquals(100, car.getEnergyValue());
    }

    @Test
    void testElectricCar() {
        car = new ElectricCar(location, energyUsageRate);
        double destination = location + 23.7;
        double energy = car.getEnergyValue();
        assertFalse(car.needsEnergy(destination));
        car.driveTo(destination);

        double distance = Math.abs(destination - location);
        energy -= distance * energyUsageRate;

        assertEquals(destination, car.getLocation());
        assertEquals(energy, car.getEnergyValue());
        assertTrue(car.needsEnergy(destination + car.energyThreshold));
    }

    @Test
    void testPetrolCar() {
        double destination = location + 23.7;
        double energy = car.getEnergyValue();
        assertFalse(car.needsEnergy(destination));
        car.driveTo(destination);

        double distance = Math.abs(destination - location);
        energy -= distance * energyUsageRate;

        assertEquals(destination, car.getLocation());
        assertEquals(energy, car.getEnergyValue());
        assertTrue(car.needsEnergy(destination + car.energyThreshold));
    }

    @Test
    void testStress() {
        double destination = location + 23.7;
        double energy = car.getEnergyValue();
        assertFalse(car.needsEnergy(destination));

        for (int i = 0; i < 100; i++) {
            if (car.needsEnergy(destination)) {
                car.refuel();
                energy = 100;
            }

            energy -= Math.abs(destination - car.getLocation()) * energyUsageRate;
            car.driveTo(destination);

            assertEquals(destination, car.getLocation());
            assertEquals(energy, car.getEnergyValue());
            destination = destination + ThreadLocalRandom.current().nextDouble(car.energyThreshold, 40);
            assertTrue(car.needsEnergy(destination));
        }
    }

}
