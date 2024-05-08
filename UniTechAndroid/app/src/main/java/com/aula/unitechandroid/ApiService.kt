package com.aula.unitechandroid

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("/api/users") // La ruta al servicio de registro en tu servidor Spring
    fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<Void>

    @FormUrlEncoded
    @POST("/api/users") // La ruta al servicio de inicio de sesión en tu servidor Spring
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<Void>

    abstract fun register(username: String, email: String): Call<Void>
}