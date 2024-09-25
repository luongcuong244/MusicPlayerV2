package com.kma.musicplayerv2.model

import java.io.Serializable

data class Playlist(
    val id: String,
    val name: String,
    val updatedAt: Long,
    val songs: MutableList<Song> = mutableListOf(),
) : Serializable