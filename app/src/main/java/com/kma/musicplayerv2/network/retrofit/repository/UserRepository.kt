package com.kma.musicplayerv2.network.retrofit.repository

import com.kma.musicplayerv2.network.common.ApiCallback
import com.kma.musicplayerv2.network.retrofit.RetrofitClient
import com.kma.musicplayerv2.network.retrofit.api.UserApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object UserRepository {
    private val userApi: UserApi = RetrofitClient.getClient().create(UserApi::class.java)

    fun changeUserName(name: String, apiCallback: ApiCallback<Boolean>) {
        apiCallback.onSuccess(
            true
        )
        return
        userApi.changeUserName(name).enqueue(
            object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if (response.isSuccessful) {
                        apiCallback.onSuccess(response.body() ?: false)
                    } else {
                        apiCallback.onFailure(response.errorBody()?.string() ?: "Unknown error")
                    }
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    apiCallback.onFailure(t.message ?: "Unknown error")
                }
            }
        )
    }
}