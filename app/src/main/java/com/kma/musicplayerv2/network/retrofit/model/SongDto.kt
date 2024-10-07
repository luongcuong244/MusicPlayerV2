package com.kma.musicplayerv2.network.retrofit.model

import com.google.gson.annotations.SerializedName
import com.kma.musicplayerv2.model.Artist
import com.kma.musicplayerv2.model.Song
import com.kma.musicplayerv2.network.common.ServerAddress
import java.util.Date

data class SongDto(
    @SerializedName("id") val id: String,
    @SerializedName("album_id") val albumId: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("title") val title: String,
    @SerializedName("file_url") val fileUrl: String,
    @SerializedName("video_url") val videoUrl: String,
    @SerializedName("views") val views: Long,
    @SerializedName("favorites") val favorites: Long,
    @SerializedName("Album") val album: AlbumDto?,
    @SerializedName("duration") val duration: Float,
    @SerializedName("created_at") val createdAt: Date,
) {
    fun toSong(): Song {
        return Song(
            id = id,
            title = title,
            artist = album?.artist?.toArtist() ?: Artist("", "Unknown Artist", ""),
            thumbnail = imageUrl.replace("localhost", ServerAddress.SERVER_IP),
            path = fileUrl.replace("localhost", ServerAddress.SERVER_IP),
            videoUrl = videoUrl.replace("localhost", ServerAddress.SERVER_IP),
            createdTime = createdAt.time,
        )
    }
}
