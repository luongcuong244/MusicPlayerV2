package com.kma.musicplayerv2.model

data class RecordingModel(
    var name: String,
    var path: String,
    var length: Long,
    var timeAdded: Long,
    var fav: Int
)