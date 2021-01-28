package com.rusanov.cranes;

import com.rusanov.port.shedule.CargoType;
import com.rusanov.port.shedule.Ship;

public  class  Crane {
    private final CargoType type;
    private boolean busy;
    private    Long ID;
    private  double unloadSpeed;
    private final double cost;

    private Ship shipToUnload;



    public Crane(CargoType type, double unloadSpeed, double cost) {
        this.type = type;
        this.busy = false;
        this.unloadSpeed = unloadSpeed;
        this.cost = cost;
    }

    public Crane(double unloadSpeed, CargoType type ) {
        this(type, unloadSpeed, 19000);
    }

    public boolean isBusy() {
        return busy;
    }

    public  void setBusy(boolean status) {
        busy = status;
    }

    public  void unload() {
        double currentWeightToUnload = shipToUnload.getCargoWeight();
        if ( currentWeightToUnload >= unloadSpeed) {
            shipToUnload.setCargoWeight(currentWeightToUnload - unloadSpeed);
        } else  {
            shipToUnload.setCargoWeight(0);
            setBusy(false);
        }
    }
}
