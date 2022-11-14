package org.jetbrains.car;

public class PetrolCar extends Car {
    public PetrolCar(double location, double energyUsageRate) {
        super(location, energyUsageRate);
        energyThreshold = 20;
    }

    public PetrolCar(Car car) {
        super(car);
        this.energyThreshold = car.energyThreshold;
    }
}
