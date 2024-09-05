package com.kma.musicplayerv2.network.retrofit.model

import com.google.gson.annotations.SerializedName
import com.kma.musicplayerv2.model.SongComment

data class SongCommentDto(
    @SerializedName("id") val id: Long,
    @SerializedName("songId") val songId: Long,
    @SerializedName("userId") val userId: Long,
    @SerializedName("userAvatar") val userAvatar: String,
    @SerializedName("userName") val userName: String,
    @SerializedName("content") val content: String,
    @SerializedName("createdAt") val createdAt: String,
) {
    fun toSongComment(): SongComment {
        return SongComment(
            id = id,
            songId = songId,
            userId = userId,
            userAvatar = userAvatar,
            userName = userName,
            content = content,
            createdAt = createdAt,
        )
    }
}
