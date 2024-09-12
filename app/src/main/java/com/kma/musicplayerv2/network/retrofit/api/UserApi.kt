package com.kma.musicplayerv2.network.retrofit.api

import com.kma.musicplayerv2.network.retrofit.model.PlaylistDto
import retrofit2.Call
import retrofit2.http.POST

interface UserApi {
    @POST("app/change-user-name")
    fun changeUserName(userName: String): Call<Boolean>
}