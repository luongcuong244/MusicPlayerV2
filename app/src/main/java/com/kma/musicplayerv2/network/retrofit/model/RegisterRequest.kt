package com.kma.musicplayerv2.network.retrofit.model

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("email") val email: String,
    @SerializedName("userName") val userName: String,
    @SerializedName("password") val password: String,
    @SerializedName("passwordRe") val passwordRe: String,
)
