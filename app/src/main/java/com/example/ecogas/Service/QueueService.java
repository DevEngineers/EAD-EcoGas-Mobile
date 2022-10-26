package com.example.ecogas.Service;

import com.example.ecogas.Model.Fuel;
import com.example.ecogas.Model.Queues;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface QueueService {

    @GET("Queue")
    Call<List<Queues>> getAllQueueDetails();

    @GET("{id}")
    Call<Queues> getQueueDetails(@Path("id") String id);

    @POST("Queue")
    Call<Queues> addUserToQueue(@Body Queues queues);

    @PUT("{id}")
    Call<Queues> updateQueueStatus(@Path("id") String id, @Body Queues queues);
}
