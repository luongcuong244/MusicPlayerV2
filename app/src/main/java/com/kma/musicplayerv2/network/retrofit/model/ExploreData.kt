package com.kma.musicplayerv2.network.retrofit.model

import com.google.gson.annotations.SerializedName

data class ExploreData(
    @SerializedName("songForYou") val songForYou: List<SongDto>,
    @SerializedName("category") val categories: List<CategoryDto>,
    @SerializedName("ablum") val album: List<AlbumDto>,
)
