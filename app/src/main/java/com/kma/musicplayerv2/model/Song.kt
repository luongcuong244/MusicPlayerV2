package com.kma.musicplayerv2.model

import java.io.Serializable
import kotlin.random.Random

data class Song(
    val id: String,
    val title: String,
    val artist: Artist,
    val path: String,
    val thumbnail: String,
    val videoUrl: String,
    val createdTime: Long,
    var isDownloaded: Boolean = false,
    var isPremium: Boolean = false
) : Serializable