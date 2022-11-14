# TestAssignment

## Requirements
- Java 11
- Maven

## Changes:
* <table>
    <tr>
    <th>Incorrect</th>
    <th>Correct</th>
    </tr>
    <tr>
    <td>
    <pre>
    public double getEnergy() {
        return ++energy;
    }
    </pre>
    </td>
    <td>
    <pre>
    public double getEnergy() {
        return energy;
    }
    </pre>
    </td>
    </tr>
    </table>

* <table>
    <tr>
    <th>Incorrect</th>
    <th>Correct</th>
    </tr>
    <tr>
    <td>
    <pre>
    double distance = destination - this.location;
    </pre>
    </td>
    <td>
    <pre>
    double distance = Math.abs(destination - this.location);
    </pre>
    </td>
    </tr>
    </table>

* Added checks to functions:
    <table>
    <tr>
    <th>getClosestChargingStation</th>
    <th>getClosestGasStation</th>
    </tr>
    <tr>
    <td>
    <pre>
    if (!(car instanceof ElectricCar)) {
        throw new IllegalArgumentException("car must be electric");
    }
    </pre>
    </td>
    <td>
    <pre>
    if (!(car instanceof PetrolCar)) {
        throw new IllegalArgumentException("car must be petrol");
    }
    </pre>
    </td>
    </tr>
    </table>

* It may be that we can't get to the station:
    ```
    private void addEnergy(double destinationLocation) {
        double destination;
        
        ...
        
        if (car.needsEnergy(destination)) {
            throw new Exception("it's not possible");
        }
        
        ...
    }
    ```

* Let's write `while` instead `if`, since, for example, we want to get from St. Petersburg to Krasnodar. We can't just refuel at the beginning of the journey, and then drive without recharging. We need to refuel several times.

    ```
    private void drive(double destinationLocation) {
        ...
        
        while (car.needsEnergy(destinationLocation)) {
            ...
        }
        
        ...
    }
    ```

    For this to work, if we have energy == 100, then we have to go to the next nearest station:
    ```
    private Station getClosestStation(Car car, List<Station> stations, double destinationLocation) {
        ...
        for (Station chargingStation : stations) {
            double destination = Math.abs(car.getLocation() - chargingStation.getLocation());
            if (car.getEnergyValue() == 100.0 && car.getLocation() != destinationLocation) {
                if (destination > 0) {
                    double diffCarLocationAndEndLocation = Math.abs(car.getLocation() - destinationLocation);
                    double diffStationLocationAndEndLocation = Math.abs(destinationLocation - chargingStation.getLocation());
                    if (diffStationLocationAndEndLocation < diffCarLocationAndEndLocation) {
                        if (destination < minDestination) {
                            closestChargingStation = chargingStation;
                            minDestination = destination;
                        }
                    }
                }
            } else {
                ...
            }
        }
        ...
    }
    ```

  In addition, we want to go to the nearest next station in the right direction. Therefore, we will pass the endpoint to the function.

  If we have 100 energy but at the same time we will not be able to get to the station in the right direction, then we could go in the other direction to the station. We'll come and refuel. And we won't be able to get to the next one - because even then we couldn't. So we won't be able to get to the end point. So there is no point in going in another direction in this case.

* There may also be such a case: home and work are very close. And we go back and forth often. We do not refuel because the distance is very small, but at the same time the energy decreases with each trip. And there will come a day when we need to refuel, but the station is further away than home or work.

    To prevent this, we need to check whether we can get to the nearest station after the trip. And if not, then refuel first:
    
    ```
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
    ```
  
* Perhaps it is necessary to add a check that the location should be positive. But it depends on the more detailed context.м