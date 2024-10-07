package com.kma.musicplayerv2.network.retrofit.api

import com.kma.musicplayerv2.network.retrofit.model.ExploreResponse
import com.kma.musicplayerv2.network.retrofit.model.GetUserInfoResponse
import com.kma.musicplayerv2.network.retrofit.model.PlaylistDto
import com.kma.musicplayerv2.network.retrofit.model.UserDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ExploreApi {

    @GET("/api/explore/v1")
    fun fetchExplore(@Query("userId") userId: String, ): Call<ExploreResponse>
}