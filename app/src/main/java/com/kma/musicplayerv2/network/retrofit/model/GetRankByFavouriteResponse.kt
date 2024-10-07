package com.kma.musicplayerv2.network.retrofit.model

import com.google.gson.annotations.SerializedName
import com.kma.musicplayerv2.model.Song

data class GetRankByFavouriteResponse(
    @SerializedName("data") var data: List<Song>,
)
