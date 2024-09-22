package com.kma.musicplayerv2.network.retrofit.model

import com.google.gson.annotations.SerializedName
import com.kma.musicplayerv2.model.Playlist

data class PlaylistDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    //@SerializedName("PlaylistSongs") val songs: Song,
) {
    fun toPlaylist(): Playlist {
        return Playlist(
            id = id,
            name = name,
        )
    }
}
