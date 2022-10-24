package com.example.ecogas;

import android.app.Application;

public class SessionApplication extends Application {

    private static String userID;
    private static String userName;
    private static String userType;
    private static String stationID;
    private static String API_URL;


    @Override
    public void onCreate() {
        super.onCreate();
        userID="";
        userName="";
        userType="";
        stationID="";
        API_URL="http://192.168.1.5:29193/";
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
    

    public static String getApiUrl() {
        return API_URL;
    }

    public static void setApiUrl(String apiUrl) {
        API_URL = apiUrl;
    }
}
