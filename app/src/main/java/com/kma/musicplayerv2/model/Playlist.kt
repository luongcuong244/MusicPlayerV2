package com.kma.musicplayerv2.model

import java.io.Serializable

data class Playlist(
    val id: Long = 0,
    val name: String = "",
    var totalSong: Int = 0,
    val image: String = "",
) : Serializable