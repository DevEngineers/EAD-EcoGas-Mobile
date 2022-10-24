package com.example.ecogas.Service;
import androidx.annotation.BinderThread;

import com.example.ecogas.Model.Fuel;
import com.example.ecogas.Model.Station;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface StationService {

    @GET("Station")
    Call<List<Station>> getAllStationDetails();

    @GET("{ownerID}")
    Call<Station> getStationDetails(@Path("ownerID") String ownerID);

    @POST("Station")
    Call<Station> createNewStation(@Body Station station);

    @PUT("{id}")
    Call<Station> updateFuelStatus(@Path("id") String id, @Body Fuel fuel);
}
