package com.kma.musicplayerv2.model

data class Category(
    val id: String,
    val name: String,
    val albums: List<Album>,
)
