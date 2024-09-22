package com.kma.musicplayerv2.network.retrofit.model

import com.google.gson.annotations.SerializedName

data class GetAllPlaylistResponse(
    @SerializedName("data") var data: List<PlaylistDto>,
)
