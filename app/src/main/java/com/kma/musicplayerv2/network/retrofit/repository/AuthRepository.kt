package com.kma.musicplayerv2.network.retrofit.repository

import com.kma.musicplayerv2.network.retrofit.model.LoginRequest
import com.kma.musicplayerv2.network.retrofit.model.LoginResponse
import com.kma.musicplayerv2.network.retrofit.model.RegisterRequest
import com.kma.musicplayerv2.network.retrofit.RetrofitClient
import com.kma.musicplayerv2.network.retrofit.api.AuthApi
import retrofit2.Callback

class AuthRepository {

    private val authApi: AuthApi = RetrofitClient.getClient().create(AuthApi::class.java)

    fun checkExistingUser(email: String, callback: Callback<Boolean>) {
        authApi.checkExistingUser(email).enqueue(callback)
    }

    fun login(loginRequest: LoginRequest, callback: Callback<LoginResponse>) {
        authApi.login(loginRequest).enqueue(callback)
    }

    fun register(registerRequest: RegisterRequest, callback: Callback<Boolean>) {
        authApi.register(registerRequest).enqueue(callback)
    }
}