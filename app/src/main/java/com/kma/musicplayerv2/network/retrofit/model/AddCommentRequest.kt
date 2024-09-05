package com.kma.musicplayerv2.network.retrofit.model

import com.google.gson.annotations.SerializedName

data class AddCommentRequest(
    @SerializedName("songId") val songId: Long,
    @SerializedName("content") val content: String,
)