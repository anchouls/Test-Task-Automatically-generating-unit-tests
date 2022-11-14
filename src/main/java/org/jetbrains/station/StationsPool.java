package org.jetbrains.station;

import org.jetbrains.car.Car;
import org.jetbrains.car.ElectricCar;
import org.jetbrains.car.PetrolCar;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class StationsPool {

    private static StationsPool stations;

    private final List<Station> gasStations = new ArrayList<>();
    private final List<Station> chargingStations = new ArrayList<>();

    private StationsPool() {
        // Add gas stations
        gasStations.add(new GasStation(1, 10));
        gasStations.add(new GasStation(2, 25));
        gasStations.add(new GasStation(3, 45));
        gasStations.add(new GasStation(4, 67));
        gasStations.add(new GasStation(5, 77));
        gasStations.add(new GasStation(6, 89));
        gasStations.add(new GasStation(7, 97));
        // Add charging stations
        chargingStations.add(new ChargingStation(8, 15));
        chargingStations.add(new ChargingStation(9, 35));
        chargingStations.add(new ChargingStation(10, 47));
        chargingStations.add(new ChargingStation(11, 59));
        chargingStations.add(new ChargingStation(12, 70));
        chargingStations.add(new ChargingStation(13, 86));
        chargingStations.add(new ChargingStation(14, 96));
    }

    public static StationsPool getInstance() {
        if (stations == null) {
            stations = new StationsPool();
        }

        return stations;
    }

    public ChargingStation getClosestChargingStation(Car car, double destinationLocation) {
        if (!(car instanceof ElectricCar)) {
            throw new IllegalArgumentException("car must be electric");
        }
        return (ChargingStation) getClosestStation(car, this.chargingStations, destinationLocation);
    }

    public GasStation getClosestGasStation(Car car, double destinationLocation) {
        if (!(car instanceof PetrolCar)) {
            throw new IllegalArgumentException("car must be petrol");
        }
        return (GasStation) getClosestStation(car, this.gasStations, destinationLocation);
    }

    private Station getClosestStation(Car car, List<Station> stations, double destinationLocation) {

        double minDestination = 100;
        Station closestChargingStation = null;

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
                if (destination < minDestination) {
                    closestChargingStation = chargingStation;
                    minDestination = destination;
                }
            }
        }

        return closestChargingStation;
    }
}
