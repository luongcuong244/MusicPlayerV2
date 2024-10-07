package com.kma.musicplayerv2.network.retrofit.model

import com.google.gson.annotations.SerializedName

data class UnhideSongRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("songIds") val songIds: List<String>
)
