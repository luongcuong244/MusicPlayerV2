package com.kma.musicplayerv2.network.retrofit.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.kma.musicplayerv2.model.SongComment
import com.kma.musicplayerv2.model.User
import java.text.SimpleDateFormat
import java.util.Date

data class SongCommentDto(
    @SerializedName("id") val id: String,
    @SerializedName("song_id") val songId: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("content") val content: String,
    @SerializedName("User") val user: UserDto,
    @SerializedName("created_at") val createdAt: Date,
) {
    @SuppressLint("SimpleDateFormat")
    fun toSongComment(): SongComment {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return SongComment(
            id = id,
            songId = songId,
            userId = userId,
            userAvatar = user.avatar,
            userName = user.fullName,
            content = content,
            createdAt = formatter.format(createdAt),
        )
    }

    @SuppressLint("SimpleDateFormat")
    fun toSongComment(user: User): SongComment {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return SongComment(
            id = id,
            songId = songId,
            userId = userId,
            userAvatar = user.avatar,
            userName = user.userName,
            content = content,
            createdAt = formatter.format(createdAt),
        )
    }
}
