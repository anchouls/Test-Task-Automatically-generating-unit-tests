package org.jetbrains.car;

public abstract class Car {

    protected double location;
    protected int energyThreshold;
    private final Energy energy;
    private final double energyUsageRate;


    public Car(double location, double energyUsageRate) {
        this.location = location;

        if (energyUsageRate <= 0) {
            throw new IllegalArgumentException("energy usage rate should be higher than 0.");
        }
        this.energyUsageRate = energyUsageRate;
        energy = new Energy();
    }

    public Car(Car car) {
        this.location = car.location;
        this.energyUsageRate = car.energyUsageRate;
        this.energy = new Energy(car.energy);
        this.energyThreshold = car.energyThreshold;
    }

    public boolean needsEnergy(double destination) {
        double distance = Math.abs(destination - this.location);
        double estimatedUsage = distance * energyUsageRate;
        return (this.energy.getEnergy() - estimatedUsage <= this.energyThreshold);
    }

    public void driveTo(double destination) {
        double distance = Math.abs(destination - this.location);
        this.energy.reduceEnergy(distance * energyUsageRate);
        this.location = destination;
    }

    public void refuel() {
        System.out.println("Refueling");
        this.energy.recharge();
    }

    public double getLocation() {
        return location;
    }

    public double getEnergyValue() {
        return (this.energy.getEnergy());
    }

    protected class Energy {
        private double energy;

        public Energy() {
            energy = 100;
        }

        public Energy(Energy energy) {
            this.energy = energy.energy;
        }

        public void reduceEnergy(double value) {
            energy -= value;
        }

        public double getEnergy() {
            return energy;
        }

        public void recharge() {
            energy = 100;
        }
    }
}
