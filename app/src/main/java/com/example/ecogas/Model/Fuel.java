package com.example.ecogas.Model;

public class Fuel {

    private String id;
    private String fuelName;
    private String capacity;
    private String arrivalDate;
    private String arrivalTime;

    public Fuel() {
    }

    public Fuel(String id, String fuelName, String capacity, String arrivalDate, String arrivalTime) {
        this.id = id;
        this.fuelName = fuelName;
        this.capacity = capacity;
        this.arrivalDate = arrivalDate;
        this.arrivalTime = arrivalTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFuelName() {
        return fuelName;
    }

    public void setFuelName(String fuelName) {
        this.fuelName = fuelName;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}
