package com.kma.musicplayerv2.model

data class SongComment(
    val id: Long,
    val songId: Long,
    val userId: Long,
    val userAvatar: String,
    val userName: String,
    val content: String,
    val createdAt: String,
)
