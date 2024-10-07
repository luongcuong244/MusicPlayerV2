package com.kma.musicplayerv2.network.retrofit.model

import com.google.gson.annotations.SerializedName

data class HiddenSong(
    @SerializedName("Song") var song: SongDto,
)
