package com.example.ecogas.Model;

import java.util.ArrayList;

public class Station {

    private String id;
    private String ownerID;
    private String ownerName;
    private String stationName;
    private String location;
    private ArrayList<Fuel> fuel;
    private int petrolQueue;
    private int superPetrolQueue;
    private int dieselQueue;
    private int superDieselQueue;

    public Station() { }

    public Station(String id, String ownerID, String ownerName, String stationName, String location, ArrayList<Fuel> fuel, int petrolQueue, int superPetrolQueue, int dieselQueue, int superDieselQueue) {
        this.id = id;
        this.ownerID = ownerID;
        this.ownerName = ownerName;
        this.stationName = stationName;
        this.location = location;
        this.fuel = fuel;
        this.petrolQueue = petrolQueue;
        this.superPetrolQueue = superPetrolQueue;
        this.dieselQueue = dieselQueue;
        this.superDieselQueue = superDieselQueue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<Fuel> getFuel() {
        return fuel;
    }

    public void setFuel(ArrayList<Fuel> fuel) {
        this.fuel = fuel;
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

    public int getSuperDieselQueue() {
        return superDieselQueue;
    }

    public void setSuperDieselQueue(int superDieselQueue) {
        this.superDieselQueue = superDieselQueue;
    }
}
