package com.example.ecogas.Service;
import com.example.ecogas.Model.Fuel;
import com.example.ecogas.Model.Station;

import retrofit2.Call;
import retrofit2.http.*;

public interface StationService {

    @GET("{id}")
    Call<Station> getStationDetails(@Path("id") String id);

    @PUT("{id}")
    Call<Fuel> updateFuelStatus(@Path("id") String id, @Body Fuel fuel); //id should be ownerID aka userID
}
