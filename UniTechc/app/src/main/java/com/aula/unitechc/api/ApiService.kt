package com.aula.unitechc.api

import com.aula.unitechc.model.ApiResponse
import com.aula.unitechc.model.Universidad
import com.aula.unitechc.model.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @DELETE("api/universidades/{id}")
    fun deleteUniversity(@Path("id") id: Long): Call<Void>
    @PUT("api/universidades/{id}")
     fun updateUniversity(
        @Path("id") id: Long,
        @Body updatedUniversity: Universidad
    ): Call<Universidad>

    @GET("api/universidades/{id}")
    fun getUniversityById(@Path("id") id: Long): Call<Universidad>

    @GET("api/universidades")
    fun getUniversidades(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String
    ): Call<ApiResponse>
// Aquí Universidad es tu clase de modelo para las universidades


    @GET("/api/users/username/{username}")
    fun getUserByUsername(@Path("username") username: String): Call<User>

    @POST("{userId}/associateUniversity/{universityId}")
    fun associateUserWithUniversity(
        @Path("userId") userId: Long,
        @Path("universityId") universityId: Long
    ): Call<Unit>

    @POST("api/universidades")
    fun createUniversity(@Body university: Universidad): Call<Universidad>

    @POST("/api/users")
    fun registerUser(@Body user: User): Call<Void>

    @POST("/api/register")
    fun register(
        @Query("username") username: String,
        @Query("email") email: String,
        @Query("password") password: String
    ): Call<Void>
}
