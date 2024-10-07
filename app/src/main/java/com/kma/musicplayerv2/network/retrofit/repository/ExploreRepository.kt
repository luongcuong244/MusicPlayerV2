package com.kma.musicplayerv2.network.retrofit.repository

import com.kma.musicplayerv2.globalstate.CurrentUser
import com.kma.musicplayerv2.network.retrofit.RetrofitClient
import com.kma.musicplayerv2.network.retrofit.api.ExploreApi
import com.kma.musicplayerv2.network.retrofit.model.ExploreResponse
import retrofit2.Callback

object ExploreRepository {

    private val exploreApi: ExploreApi = RetrofitClient.getClient().create(ExploreApi::class.java)

    fun fetchExplore(callback: Callback<ExploreResponse>) {
        exploreApi.fetchExplore(CurrentUser.getUser()!!.id).enqueue(callback)
    }
}