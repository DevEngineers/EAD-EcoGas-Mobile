package com.example.ecogas.Service;

import com.example.ecogas.Model.Queues;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * This interface is to call ecogas rest api http methods in the backend api
 * @Author: IT19175126 Zumry A.M
 */


public interface QueueService {

    @GET("Queue")
    Call<List<Queues>> getAllQueueDetails();

    @GET("{id}")
    Call<Queues> getQueueDetails(@Path("id") String id);

    @GET("User/{id}")
    Call<Queues> getQueueByUserID(@Path("id") String id);

    @POST("Queue")
    Call<Queues> addUserToQueue(@Body Queues queues);

    @DELETE("Queue/{id}")
    Call<Queues> removeUserInQueue(@Path("id") String id);

    @PUT("{id}")
    Call<Queues> updateQueueStatus(@Path("id") String id, @Body Queues queues);
}
