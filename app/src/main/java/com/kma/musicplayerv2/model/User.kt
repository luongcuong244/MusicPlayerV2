package com.kma.musicplayerv2.model

data class User(
    var id: String,
    var email: String,
    var userName: String?,
    var avatar: String?,
    var role: Int = 0
)
