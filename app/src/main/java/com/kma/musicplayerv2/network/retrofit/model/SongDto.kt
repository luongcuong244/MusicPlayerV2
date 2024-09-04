package com.kma.musicplayerv2.network.retrofit.model

import com.google.gson.annotations.SerializedName
import com.kma.musicplayerv2.model.Artist
import com.kma.musicplayerv2.model.Song

data class SongDto(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("artist") val artist: Artist,
    @SerializedName("duration") val duration: Int,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("path") val path: String,
    @SerializedName("isFavourite") val isFavourite: Boolean,
    @SerializedName("isPremium") val isPremium: Boolean
) {
    fun toSong(): Song {
        return Song(
            id = id,
            title = title,
            artist = artist,
            thumbnail = thumbnail,
            path = path,
            isFavourite = isFavourite,
            isPremium = isPremium
        )
    }
}
