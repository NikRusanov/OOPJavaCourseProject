package com.rusanov.cranes;

import com.rusanov.port.CargoType;
import com.rusanov.port.Ship;

public class Crane {


    private final CargoType type;
    private boolean busy;
    private Long id;
    private double unloadSpeed;
    private final long cost;

    private Ship shipToUnload;


    public long getCost() {
        return cost;
    }

    public Crane(CargoType type, double unloadSpeed, long cost) {
        this.type = type;
        this.busy = false;
        this.unloadSpeed = unloadSpeed;
        this.cost = cost;
    }

    public Crane(double unloadSpeed, CargoType type) {
        this(type, unloadSpeed, 19000);
    }

    public Crane(CargoType type, Long id) {
        this(type);
        this.id = id;
    }

    public Crane(CargoType type) {
        this.type = type;
        busy = false;
        cost = 19000;
        unloadSpeed = type.getLoadSpeed();
    }


    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean status) {
        busy = status;
    }

    public void setShip(Ship ship) {
        this.shipToUnload = ship;
        setBusy(true);
    }

    public void unload() {
        double currentWeightToUnload = shipToUnload.getCargoWeight();
        if (currentWeightToUnload >= unloadSpeed) {
            shipToUnload.setCargoWeight(currentWeightToUnload - unloadSpeed);
        } else {
            shipToUnload.setCargoWeight(0);
        }
    }

    public Ship getShip() {
        return shipToUnload;
    }


    @Override
    public String toString() {
        return "\n" +
                "type:" + type.getTypeName() +
                "\n busy: " + busy +
                "\n, ID: " + id +
                "\n unloadSpeed: " + unloadSpeed +
                "\n cost: " + cost +
                "\n shipToUnload :" + shipToUnload +
                '\n';
    }
}
