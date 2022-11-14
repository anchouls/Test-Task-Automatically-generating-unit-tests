package org.jetbrains.person;


import org.jetbrains.car.Car;
import org.jetbrains.car.PetrolCar;
import org.jetbrains.station.GasStation;
import org.jetbrains.station.StationsPool;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;
import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    @Test
    void testPerson(){
        Car car = new PetrolCar(10,2);
        Person person = new Person(19,10.1,46.10, car);
        person.goToWork();
        person.goToHome();
        person.goToWork();
        person.goToHome();
        person.goToWork();

        assert (car.getEnergyValue() > 0 && car.getEnergyValue() <=100);
    }

    @Test
    void testStress(){
        for (double i = 0; i < 10; i++) {
            double location = ThreadLocalRandom.current().nextDouble(0, 100);
            double energyUsageRate = ThreadLocalRandom.current().nextDouble(1, 2);
            int age = 19;
            double homeLocation = ThreadLocalRandom.current().nextDouble(0, 100);
            double workLocation = ThreadLocalRandom.current().nextDouble(0, 100);

            System.out.println("location: "+ location + " rate: " + energyUsageRate + " home: " + homeLocation + " work: " + workLocation);

            Car car = new PetrolCar(location, energyUsageRate);
            Person person = new Person(age, homeLocation, workLocation, car);

            for (double j = 0; j < 10; j++) {
                if (checkNotPossible(car, workLocation)) {
                    assertThrows(IllegalArgumentException.class, person::goToWork);
                    break;
                } else {
                    person.goToWork();
                    assert (car.getEnergyValue() > 0 && car.getEnergyValue() <= 100);
                }
                if (checkNotPossible(car, workLocation)) {
                    assertThrows(IllegalArgumentException.class, person::goToHome);
                    break;
                } else {
                    person.goToHome();
                    assert (car.getEnergyValue() > 0 && car.getEnergyValue() <= 100);
                }
            }
        }
    }

    @Test
    void testNotPossible(){
        double location = 9.9;
        double energyUsageRate = 1.5;
        int age = 19;
        double homeLocation = 32.57;
        double workLocation = 59.0;

        Car car = new PetrolCar(location, energyUsageRate);
        Person person = new Person(age, homeLocation, workLocation, car);

        person.goToWork();
        assert (car.getEnergyValue() > 0 && car.getEnergyValue() <= 100);

        assertThrows(IllegalArgumentException.class, person::goToHome);
    }

    boolean checkNotPossible(Car car, double destinationLocation) {
        GasStation gasStation = StationsPool.getInstance().getClosestGasStation(car, destinationLocation);
        if (gasStation == null) return false;
        double destination = gasStation.getLocation();
        return car.needsEnergy(destination);
    }

    @Test
    void testBigEnergyUsageRate(){
        double location = 43.1;
        double energyUsageRate = 5.0;
        int age = 19;
        double homeLocation = 43.1;
        double workLocation = 68.9;

        Car car = new PetrolCar(location, energyUsageRate);
        Person person = new Person(age, homeLocation, workLocation, car);

        assertThrows(IllegalArgumentException.class, person::goToWork);
    }

    @Test
    void testSmallDistanceBetweenHomeAndWork(){
        double location = 89.61;
        double energyUsageRate = 2.0;
        int age = 19;
        double homeLocation = 89.61;
        double workLocation = 93.64;

        Car car = new PetrolCar(location, energyUsageRate);
        Person person = new Person(age, homeLocation, workLocation, car);

        for (double j = 0; j < 10; j++) {
            person.goToWork();
            assert (car.getEnergyValue() > 0 && car.getEnergyValue() <= 100);
            person.goToHome();
            assert (car.getEnergyValue() > 0 && car.getEnergyValue() <= 100);
        }
        person.goToWork();

        assert (car.getEnergyValue() > 0 && car.getEnergyValue() <= 100);
    }

}