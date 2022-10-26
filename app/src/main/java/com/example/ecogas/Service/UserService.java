package com.example.ecogas.Service;


import com.example.ecogas.Model.User;

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
 *  Author: IT19167442 Nusky M.A.M
 */

public interface UserService {

    @GET("user")
    Call<List<User>> getAllUserDetails();

    @GET("{id}")
    Call<User> getUserDetails(@Path("id") String id);

    @GET("{name}")
    Call<User> getUserDetailsByName(@Path("name") String name);

    @POST("user")
    Call<User> createUser(@Body User user);

    @PUT("{id}")
    Call<User> updateUserDetails(@Path("id") String id, @Body User user);

    @DELETE("{id}")
    Call<User> deleteUserDetails(@Path("id") String id);
}
