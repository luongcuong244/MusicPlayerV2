package com.kma.musicplayerv2.model

import java.io.Serializable

data class Artist(
    val id: String,
    val name: String,
    val image: String,
) : Serializable