package com.kma.musicplayerv2.network.retrofit.model

import com.google.gson.annotations.SerializedName

data class CreatePlaylistRequest(
    @SerializedName("name") val playlistName: String,
    @SerializedName("userId") val userId: String,
)