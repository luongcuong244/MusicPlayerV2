package com.kma.musicplayerv2.network.retrofit.api

import com.kma.musicplayerv2.network.retrofit.model.GetUserInfoResponse
import com.kma.musicplayerv2.network.retrofit.model.PlaylistDto
import com.kma.musicplayerv2.network.retrofit.model.UserDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApi {

    @GET("/api/user/v1/profile")
    fun getUserInfo(): Call<GetUserInfoResponse>

    @POST("app/change-user-name")
    fun changeUserName(userName: String): Call<Boolean>
}