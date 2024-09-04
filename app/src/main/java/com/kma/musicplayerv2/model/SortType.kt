package com.kma.musicplayerv2.model

import com.kma.musicplayerv2.R

enum class SortType(
    val textId: Int
) {
    NEWEST(R.string.newest),
    OLDEST(R.string.oldest),
    SONG_NAME(R.string.song_name_a_z),
    ARTIST_NAME(R.string.artist_name_a_z),
}