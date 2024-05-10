package com.aula.unitechc.api;

import com.aula.unitechc.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @GET("/api/users")
    Call<List<User>> getUsers();

    @GET("/api/user")
    Call<User> getUserByEmail(@Query("email") String email);

    @POST("/api/users")
    Call<Void> registerUser(@Body User user);

    @POST("/api/register")
    Call<Void> register(
            @Query("username") String username,
            @Query("email") String email,
            @Query("password") String password
    );
}
