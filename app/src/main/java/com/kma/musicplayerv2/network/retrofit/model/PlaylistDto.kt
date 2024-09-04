package com.kma.musicplayerv2.network.retrofit.model

import com.google.gson.annotations.SerializedName
import com.kma.musicplayerv2.model.Playlist

data class PlaylistDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("totalSong") val totalSong: Int,
    @SerializedName("image") val image: String,
) {
    fun toPlaylist(): Playlist {
        return Playlist(
            id = id,
            name = name,
            totalSong = totalSong,
            image = image
        )
    }
}
