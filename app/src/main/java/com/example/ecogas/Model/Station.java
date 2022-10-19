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
}
