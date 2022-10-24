package com.example.ecogas;

import android.app.Application;

public class SessionApplication extends Application {

    private static String userID;
    private static String userName;
    private static String userType;
    private static String userEmail;
    private static String stationID;


    @Override
    public void onCreate() {
        super.onCreate();
        userID="";
        userName="";
        userType="";
        userEmail="";
        stationID="";
    }


    public static String getStationID() { return stationID; }

    public static void setStationID(String stationID) { SessionApplication.stationID = stationID; }

    public static String getUserID() {
        return userID;
    }

    public static void setUserID(String userID) {
        SessionApplication.userID = userID;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        SessionApplication.userName = userName;
    }

    public static String getUserType() {
        return userType;
    }

    public static void setUserType(String userType) {
        SessionApplication.userType = userType;
    }

    public static String getUserEmail() {
        return userEmail;
    }

    public static void setUserEmail(String userEmail) {
        SessionApplication.userEmail = userEmail;
    }
}
