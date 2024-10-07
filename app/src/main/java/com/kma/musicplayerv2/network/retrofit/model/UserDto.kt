package com.kma.musicplayerv2.network.retrofit.model

import com.google.gson.annotations.SerializedName
import com.kma.musicplayerv2.model.User

data class UserDto(
    @SerializedName("id") var id: String,
    @SerializedName("fullName") var fullName: String?,
    @SerializedName("email") var email: String,
    @SerializedName("role") var role: Int,
    @SerializedName("phone") var phone: String?,
    @SerializedName("address") var address: String?,
    @SerializedName("avatar") var avatar: String?,
) {
    fun toUser(): User {
        return User(
            id = id,
            email = email,
            role = role,
            avatar = avatar,
            userName = fullName,
        )
    }
}