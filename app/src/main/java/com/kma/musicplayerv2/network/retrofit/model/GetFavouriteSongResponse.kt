package com.kma.musicplayerv2.network.retrofit.model

import com.google.gson.annotations.SerializedName

data class GetFavouriteSongResponse(
    @SerializedName("data") var data: List<Map<String, SongDto>>,
)
