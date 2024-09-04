package com.kma.musicplayerv2.network.retrofit

import com.google.gson.GsonBuilder
import com.kma.musicplayerv2.network.common.ServerAddress
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    companion object {

        private var retrofit: Retrofit? = null

        fun getClient(): Retrofit {
            if (retrofit == null) {
                createClient()
            }
            return retrofit!!
        }

        private fun createClient() {
            val gson = GsonBuilder()
                .setLenient()
                .create()

            retrofit = Retrofit.Builder()
                .baseUrl(ServerAddress.SERVER_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(OkHttpClient.Builder().build())
                .build()
        }
    }
}