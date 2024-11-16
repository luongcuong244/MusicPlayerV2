package com.kma.musicplayerv2.model

data class SongComment(
    val id: String,
    val songId: String,
    val userId: String,
    val userAvatar: String?,
    val userName: String?,
    val content: String,
    val createdAt: String,
)
