package com.kma.musicplayerv2.network.retrofit.api

import com.kma.musicplayerv2.network.retrofit.model.GetRankByFavouriteResponse
import com.kma.musicplayerv2.network.retrofit.model.GetRankByViewResponse
import retrofit2.Call
import retrofit2.http.GET

interface RankApi {
    @GET("/api/rank/get-by-view")
    fun getRankByView(): Call<GetRankByViewResponse>

    @GET("/api/rank/v1/get-by-favourite")
    fun getRankByFavourite(): Call<GetRankByFavouriteResponse>
}