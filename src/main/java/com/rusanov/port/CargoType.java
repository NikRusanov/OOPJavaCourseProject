package com.rusanov.port;

public enum CargoType {
    LIQUID("Жидкий",24000),
    BULK("Сыпучий", 12000),
    CONTAINER("Контейнер", 18000);

    private final String type;
    private final int loadSpeed;
    CargoType(String typeName, int loadSpeed) {
        this.type = typeName;
        this.loadSpeed = loadSpeed;
    }
    public String getTypeName(){
        return type;
    }
    public int getLoadSpeed() {
        return loadSpeed;
    }
}
