package com.kma.musicplayerv2.network.retrofit.repository

import com.kma.musicplayerv2.network.retrofit.RetrofitClient
import com.kma.musicplayerv2.network.retrofit.api.RankApi
import com.kma.musicplayerv2.network.retrofit.model.GetRankByFavouriteResponse
import com.kma.musicplayerv2.network.retrofit.model.GetRankByViewResponse
import retrofit2.Callback

object RankRepository {

    private val rankApi: RankApi = RetrofitClient.getClient().create(RankApi::class.java)

    fun getRankByView(callback: Callback<GetRankByViewResponse>) {
        rankApi.getRankByView().enqueue(callback)
    }

    fun getRankByFavourite(callback: Callback<GetRankByFavouriteResponse>) {
        rankApi.getRankByFavourite().enqueue(callback)
    }
}