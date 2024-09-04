package com.kma.musicplayerv2.model

import java.io.Serializable

data class Artist(
    val id: Long,
    val name: String,
    val image: String,
) : Serializable