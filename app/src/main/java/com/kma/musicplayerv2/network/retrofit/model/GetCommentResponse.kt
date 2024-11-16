package com.kma.musicplayerv2.network.retrofit.model

import com.google.gson.annotations.SerializedName

data class GetCommentResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: List<SongCommentDto>,
)