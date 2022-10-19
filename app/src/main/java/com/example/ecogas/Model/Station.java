package com.example.ecogas.Model;

public class Station {

    private String stationID;
    private String ownerID;
    private String ownerName;
    private String stationName;
    private String stationLocation;
    private Fuel petrol;
    private Fuel superPetrol;
    private Fuel diesel;
    private int petrolQueue;
    private int superPetrolQueue;
    private int dieselQueue;

    public Station(String stationID,String stationName,String stationLocation) {
        this.stationID = stationID;
        this.stationName = stationName;
        this.stationLocation = stationLocation;
    }

    public String getStationID() {
        return stationID;
    }

    public void setStationID(String stationID) {
        this.stationID = stationID;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationLocation() {
        return stationLocation;
    }

    public void setStationLocation(String stationLocation) {
        this.stationLocation = stationLocation;
    }

    public Fuel getPetrol() {
        return petrol;
    }

    public void setPetrol(Fuel petrol) {
        this.petrol = petrol;
    }

    public Fuel getSuperPetrol() {
        return superPetrol;
    }

    public void setSuperPetrol(Fuel superPetrol) {
        this.superPetrol = superPetrol;
    }

    public Fuel getDiesel() {
        return diesel;
    }

    public void setDiesel(Fuel diesel) {
        this.diesel = diesel;
    }

    public int getPetrolQueue() {
        return petrolQueue;
    }

    public void setPetrolQueue(int petrolQueue) {
        this.petrolQueue = petrolQueue;
    }

    public int getSuperPetrolQueue() {
        return superPetrolQueue;
    }

    public void setSuperPetrolQueue(int superPetrolQueue) {
        this.superPetrolQueue = superPetrolQueue;
    }

    public int getDieselQueue() {
        return dieselQueue;
    }

    public void setDieselQueue(int dieselQueue) {
        this.dieselQueue = dieselQueue;
    }
}
