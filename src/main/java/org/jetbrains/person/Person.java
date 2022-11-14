package org.jetbrains.person;

import org.jetbrains.car.Car;
import org.jetbrains.car.ElectricCar;
import org.jetbrains.car.PetrolCar;
import org.jetbrains.station.ChargingStation;
import org.jetbrains.station.GasStation;
import org.jetbrains.station.StationsPool;

public class Person {

    private final int age;
    private final double homeLocation;
    private final double workLocation;

    private Car car;


    public Person(int age, double homeLocation, double workLocation, Car car) {
        this.age = age;
        this.homeLocation = homeLocation;
        this.workLocation = workLocation;

        if (car == null) {
            throw new IllegalArgumentException("Car is empty");
        }
        this.car = car;
    }

    public void goToWork() {
        drive(workLocation);
    }

    public void goToHome() {
        drive(homeLocation);
    }

    private void drive(double destinationLocation) {
        if (this.age < 18) {
            System.out.println("This person is too young to drive!");
            return;
        }

        while (car.needsEnergy(destinationLocation)) {
            System.out.println("Needs energy");
            addEnergy(destinationLocation);
        }
        needEnergyToStation(destinationLocation);

        System.out.println("Drive to " + destinationLocation + ". Current location " + car.getLocation() + ". Energy " + car.getEnergyValue());

        car.driveTo(destinationLocation);
    }

    private void addEnergy(double destinationLocation) {
        double destination;
        if (car instanceof ElectricCar) {
            ChargingStation chargingStation = StationsPool.getInstance().getClosestChargingStation(car, destinationLocation);
            destination = chargingStation.getLocation();
        } else {
            GasStation gasStation = StationsPool.getInstance().getClosestGasStation(car, destinationLocation);
            destination = gasStation.getLocation();
        }
        System.out.println("Drive to " + destination + ". Current location " + car.getLocation() + ". Energy " + car.getEnergyValue());

        if (car.needsEnergy(destination)) {
            throw new IllegalArgumentException("it's not possible"); // other exception
        }

        car.driveTo(destination);
        car.refuel();
    }

    public void changeCar(Car car) {
        this.car = car;
    }

    private void needEnergyToStation(double destinationLocation) {
        double destination;
        Car copyCar;
        if (car instanceof ElectricCar) {
            copyCar = new ElectricCar(car);
            copyCar.driveTo(destinationLocation);
            ChargingStation chargingStation = StationsPool.getInstance().getClosestChargingStation(copyCar, destinationLocation);
            destination = chargingStation.getLocation();
        } else {
            copyCar = new PetrolCar(car);
            copyCar.driveTo(destinationLocation);
            GasStation gasStation = StationsPool.getInstance().getClosestGasStation(copyCar, destinationLocation);
            destination = gasStation.getLocation();
        }

        if (copyCar.needsEnergy(destination)) {
            System.out.println("Needs energy");
            addEnergy(destinationLocation);
        }

    }

}
