package org.jetbrains.station;

public abstract class Station {
    private final int id;
    private final double location;

    public Station(int id, int location) {
        this.id = id;
        this.location = location;
    }

    public double getLocation() {
        return location;
    }
}
