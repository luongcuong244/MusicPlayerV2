package com.kma.musicplayerv2.model

class ListenRecently(
    val id: Int,
    val type: Type,
    val name: String,
    val image: String
) {
    enum class Type {
        SONG,
        PLAYLIST,
    }
}