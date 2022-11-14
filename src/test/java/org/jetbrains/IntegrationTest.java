package org.jetbrains;

import org.jetbrains.car.Car;
import org.jetbrains.car.ElectricCar;
import org.jetbrains.car.PetrolCar;
import org.jetbrains.person.Person;
import org.jetbrains.station.ChargingStation;
import org.jetbrains.station.GasStation;
import org.jetbrains.station.StationsPool;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntegrationTest {


    @Test
    void testGoToWork() {
        int age = 19;
        double homeLocation = 10.1;
        double workLocation = 46.1;
        double location = 10;
        double energyUsageRate = 2;
        Car car = new PetrolCar(location, energyUsageRate);
        Person person = new Person(age, homeLocation, workLocation, car);

        double distance = Math.abs(workLocation - location);
        double energy = car.getEnergyValue() - distance * energyUsageRate;

        assert (car.getEnergyValue() > 0 && car.getEnergyValue() <= 100);

        person.goToWork();

        assert (car.getEnergyValue() > 0 && car.getEnergyValue() <= 100);
        assertEquals(energy, car.getEnergyValue());
        assertEquals(workLocation, car.getLocation());
    }

    @Test
    void testGoToHome() {
        int age = 19;
        double homeLocation = 10.1;
        double workLocation = 46.1;
        double location = 30.5;
        double energyUsageRate = 2;
        Car car = new PetrolCar(location, energyUsageRate);
        Person person = new Person(age, homeLocation, workLocation, car);

        double distance = Math.abs(homeLocation - location);
        double energy = car.getEnergyValue() - distance * energyUsageRate;

        assert (car.getEnergyValue() > 0 && car.getEnergyValue() <= 100);

        person.goToHome();

        assert (car.getEnergyValue() > 0 && car.getEnergyValue() <= 100);
        assertEquals(energy, car.getEnergyValue());
        assertEquals(homeLocation, car.getLocation());
    }

    @Test
    void testStress() {
        int age = 19;
        double homeLocation = 10.1;
        double workLocation = 46.1;
        double location = 10;
        double energyUsageRate = 2;
        Car car = new PetrolCar(location, energyUsageRate);
        Person person = new Person(age, homeLocation, workLocation, car);

        for (int i = 0; i < 100; i++) {
            assert (car.getEnergyValue() > 0 && car.getEnergyValue() <= 100);

            person.goToWork();

            assert (car.getEnergyValue() > 0 && car.getEnergyValue() <= 100);
            assertEquals(workLocation, car.getLocation());

            assert (car.getEnergyValue() > 0 && car.getEnergyValue() <= 100);

            person.goToHome();

            assert (car.getEnergyValue() > 0 && car.getEnergyValue() <= 100);
            assertEquals(homeLocation, car.getLocation());
        }
    }

    @Test
    void testAddEnergyPetrolCar() {
        int age = 19;
        double homeLocation = 10.1;
        double workLocation = 46.1;
        double location = 10;
        double energyUsageRate = 2;
        Car car = new PetrolCar(location, energyUsageRate);
        Person person = new Person(age, homeLocation, workLocation, car);

        assert (car.getEnergyValue() > 0 && car.getEnergyValue() <= 100);
        double energy = car.getEnergyValue() - Math.abs(workLocation - location) * energyUsageRate;

        person.goToWork();

        assert (car.getEnergyValue() > 0 && car.getEnergyValue() <= 100);
        assertEquals(energy, car.getEnergyValue());
        assertEquals(workLocation, car.getLocation());
        location = car.getLocation();

        assert (car.getEnergyValue() > 0 && car.getEnergyValue() <= 100);
        energy -= Math.abs(homeLocation - location) * energyUsageRate;
        if (energy <= 20) {
            energy = 100;
            GasStation gasStation = StationsPool.getInstance().getClosestGasStation(car, homeLocation);
            location = gasStation.getLocation();
            energy -= Math.abs(homeLocation - location) * energyUsageRate;
        }

        person.goToHome();

        assert (car.getEnergyValue() > 0 && car.getEnergyValue() <= 100);
        assertEquals(energy, car.getEnergyValue());
        assertEquals(homeLocation, car.getLocation());
    }

    @Test
    void testAddEnergyElectricCar() {
        int age = 19;
        double homeLocation = 15.1;
        double workLocation = 36.1;
        double location = 18;
        double energyUsageRate = 2;
        Car car = new ElectricCar(location, energyUsageRate);
        Person person = new Person(age, homeLocation, workLocation, car);

        assert (car.getEnergyValue() > 0 && car.getEnergyValue() <= 100);
        double energy = car.getEnergyValue() - Math.abs(workLocation - location) * energyUsageRate;

        person.goToWork();

        assert (car.getEnergyValue() > 0 && car.getEnergyValue() <= 100);
        assertEquals(energy, car.getEnergyValue());
        assertEquals(workLocation, car.getLocation());
        location = car.getLocation();

        assert (car.getEnergyValue() > 0 && car.getEnergyValue() <= 100);
        energy -= Math.abs(homeLocation - location) * energyUsageRate;
        if (energy <= 40) {
            energy = 100;
            ChargingStation gasStation = StationsPool.getInstance().getClosestChargingStation(car, homeLocation);
            location = gasStation.getLocation();
            energy -= Math.abs(homeLocation - location) * energyUsageRate;
        }

        person.goToHome();

        assert (car.getEnergyValue() > 0 && car.getEnergyValue() <= 100);
        assertEquals(energy, car.getEnergyValue());
        assertEquals(homeLocation, car.getLocation());
    }

    @Test
    void testChangeCar() {
        int age = 19;
        double homeLocation = 10.1;
        double workLocation = 46.1;
        double location = 18;
        double energyUsageRate = 2;
        Car car = new ElectricCar(location, energyUsageRate);
        Person person = new Person(age, homeLocation, workLocation, car);

        assert (car.getEnergyValue() > 0 && car.getEnergyValue() <= 100);
        double energy = car.getEnergyValue() - Math.abs(workLocation - location) * energyUsageRate;

        person.goToWork();

        assert (car.getEnergyValue() > 0 && car.getEnergyValue() <= 100);
        assertEquals(energy, car.getEnergyValue());
        assertEquals(workLocation, car.getLocation());
        location = car.getLocation();

        Car petrolCar = new PetrolCar(location, energyUsageRate);
        person.changeCar(petrolCar);

        energy = 100;
        assertEquals(energy, petrolCar.getEnergyValue());

        energy -= Math.abs(homeLocation - location) * energyUsageRate;

        person.goToHome();

        assert (petrolCar.getEnergyValue() > 0 && petrolCar.getEnergyValue() <= 100);
        assertEquals(energy, petrolCar.getEnergyValue());
        assertEquals(homeLocation, petrolCar.getLocation());
    }


}
