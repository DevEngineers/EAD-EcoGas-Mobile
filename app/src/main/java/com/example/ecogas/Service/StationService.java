package com.example.ecogas.Service;

import com.example.ecogas.Model.Fuel;
import com.example.ecogas.Model.Station;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * This interface is to call ecogas rest api http methods in the backend api
 * Author: IT19153414 Akeel M.N.M
 */

public interface StationService {

    @GET("Station")
    Call<List<Station>> getAllStationDetails();

    @GET("{id}")
    Call<Station> getStationDetails(@Path("id") String id);

    @GET("owner/{id}")
    Call<Station> getStationByOwnerID(@Path("id") String id);

    @GET("location/{id}")
    Call<List<Station>> getStationByLocation(@Path("id") String id);

    @POST("Station")
    Call<Station> createNewStation(@Body Station station);

    @PUT("{id}")
    Call<Station> updateFuelStatus(@Path("id") String id, @Body Fuel fuel);

    @PUT("add/{id}")
    Call<Station> increaseFuelQueueCount(@Path("id") String id, @Body Fuel fuel);

    @PUT("remove/{id}")
    Call<Station> decreaseFuelQueueCount(@Path("id") String id, @Body Fuel fuel);
}
