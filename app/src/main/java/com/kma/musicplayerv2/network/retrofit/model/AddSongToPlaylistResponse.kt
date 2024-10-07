package com.kma.musicplayerv2.network.retrofit.model

import com.google.gson.annotations.SerializedName

data class AddSongToPlaylistResponse(
    @SerializedName("status") val status: Int,
)
