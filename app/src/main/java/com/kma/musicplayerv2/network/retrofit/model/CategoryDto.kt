package com.kma.musicplayerv2.network.retrofit.model

import com.google.gson.annotations.SerializedName
import com.kma.musicplayerv2.model.Category

data class CategoryDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("albums") val albums: List<AlbumDto>,
) {
    fun toCategory() = Category(
        id = id,
        name = name,
        albums = albums.map { it.toAlbum() }
    )
}