package com.aula.unitechc.api

import com.aula.unitechc.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("/api/users/username/{username}")
    fun getUserByUsername(@Path("username") username: String): Call<User>


    @POST("/api/users")
    fun registerUser(@Body user: User): Call<Void>

    @POST("/api/register")
    fun register(
        @Query("username") username: String,
        @Query("email") email: String,
        @Query("password") password: String
    ): Call<Void>
}
