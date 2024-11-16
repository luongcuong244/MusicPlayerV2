package com.kma.musicplayerv2.network.retrofit.model

import com.google.gson.annotations.SerializedName
import com.kma.musicplayerv2.model.Album
import com.kma.musicplayerv2.network.common.ServerAddress

data class AlbumDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("Artist") val artist: ArtistDto,
    @SerializedName("songs") val songs: List<SongDto>,
) {
    fun toAlbum(): Album {
        return Album(
            id = id,
            title = title.split(";")[0],
            description = title.split(";")[1],
            thumbnail = imageUrl.replace("localhost", ServerAddress.SERVER_IP),
            songs = songs.map { it.toSong() },
            createdTime = 0L,
        )
    }
}
