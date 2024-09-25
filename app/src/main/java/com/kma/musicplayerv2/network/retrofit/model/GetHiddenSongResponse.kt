package com.kma.musicplayerv2.network.retrofit.model

import com.google.gson.annotations.SerializedName

data class GetHiddenSongResponse(
    @SerializedName("data") var data: List<HiddenSong>,
)
