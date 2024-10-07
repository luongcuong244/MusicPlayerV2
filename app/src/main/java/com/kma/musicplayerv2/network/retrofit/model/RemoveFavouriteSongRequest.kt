package com.kma.musicplayerv2.network.retrofit.model

import com.google.gson.annotations.SerializedName

data class RemoveFavouriteSongRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("songId") val songId: String
)
