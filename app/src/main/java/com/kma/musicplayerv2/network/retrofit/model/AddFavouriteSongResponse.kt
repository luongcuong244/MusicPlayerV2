package com.kma.musicplayerv2.network.retrofit.model

import com.google.gson.annotations.SerializedName

data class AddFavouriteSongResponse(
    @SerializedName("success") val success: Boolean,
)
