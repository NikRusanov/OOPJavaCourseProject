package com.rusanov.port.shedule;


public class Ship {
    private String shipName;
    private Double cargoWeight;
    private CargoType type;
    public String getShipName() {
        return shipName;
    }

    public Ship(String shipName, Double cargoWeight, CargoType type) {
        this.shipName = shipName;
        this.cargoWeight = cargoWeight;
        this.type = type;
    }

    public double getCargoWeight() {
        return cargoWeight;
    }
    public CargoType getType() {
        return type;
    }
    public void setCargoWeight(double weight) {
        cargoWeight = weight;
    }

    @Override
    public String toString() {
        return String.format("""
                Судно:%s\s
                 Тип: %s\s
                 Вес: %5.2f""", shipName, type, cargoWeight);
    }

    public void setName(String name) {
        shipName = name;
    }
}
