package com.kma.musicplayerv2.network.retrofit.model

import com.google.gson.annotations.SerializedName

data class RemoveSongFromPlaylistResponse(
    @SerializedName("error") val boolean: Boolean,
)
