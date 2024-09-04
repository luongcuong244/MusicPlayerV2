package com.kma.musicplayerv2.model

import java.io.Serializable
import kotlin.random.Random

data class Song(
    val id: Long,
    val title: String,
    val artist: Artist,
    val path: String,
    val thumbnail: String,
    val createdTime: Long = System.currentTimeMillis(),
    var isFavourite: Boolean = Random.nextBoolean(),
    var isDownloaded: Boolean = Random.nextBoolean(),
    var isPremium: Boolean = Random.nextBoolean()
) : Serializable