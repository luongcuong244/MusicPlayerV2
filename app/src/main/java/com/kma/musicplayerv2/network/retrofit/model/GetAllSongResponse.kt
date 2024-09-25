package com.kma.musicplayerv2.network.retrofit.model

import com.google.gson.annotations.SerializedName

data class GetAllSongResponse(
    @SerializedName("data") val data: List<SongDto>
)
