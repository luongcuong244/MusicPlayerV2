package com.kma.musicplayerv2.model

data class Album(
    val id: String,
    val title: String,
    val thumbnail: String,
    val description: String,
    val songs: List<Song>,
    val createdTime: Long,
)