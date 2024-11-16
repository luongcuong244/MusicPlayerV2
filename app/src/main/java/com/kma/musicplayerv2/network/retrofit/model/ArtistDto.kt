package com.kma.musicplayerv2.network.retrofit.model

import com.google.gson.annotations.SerializedName
import com.kma.musicplayerv2.model.Artist

data class ArtistDto(
    @SerializedName("id") val id: String,
    @SerializedName("stage_name") val name: String,
    @SerializedName("User") val user: UserDto?,
) {
    fun toArtist(): Artist {
        return Artist(
            id = id,
            name = name,
            image = user?.avatar ?: "",
        )
    }
}
