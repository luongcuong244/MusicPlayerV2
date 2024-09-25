package com.kma.musicplayerv2.network.retrofit.model

import com.google.gson.annotations.SerializedName
import com.kma.musicplayerv2.model.Playlist
import java.util.Date

data class PlaylistDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("updated_at") val updatedAt: Date,
    @SerializedName("PlaylistSongs") val songs: List<PlaylistSongs>?,
) {
    fun toPlaylist(): Playlist {
        return Playlist(
            id = id,
            name = name,
            updatedAt = updatedAt.time,
            songs = songs?.map { it.song.toSong() }?.toMutableList() ?: mutableListOf()
        )
    }
}
