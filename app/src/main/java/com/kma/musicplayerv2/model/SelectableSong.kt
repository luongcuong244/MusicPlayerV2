package com.kma.musicplayerv2.model

data class SelectableSong(
    val song: Song,
    var isSelected: Boolean = false
)
