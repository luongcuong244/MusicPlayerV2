package com.kma.musicplayerv2.network.retrofit.api

import com.kma.musicplayerv2.network.retrofit.model.RegisterRequest
import com.kma.musicplayerv2.network.retrofit.model.LoginRequest
import com.kma.musicplayerv2.network.retrofit.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {
    @GET("auth/user/check-existing-user")
    fun checkExistingUser(@Query("email") email: String): Call<Boolean>

    @POST("auth/user/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("auth/user/register")
    fun register(@Body request: RegisterRequest): Call<Boolean>
}