package com.kma.musicplayerv2.network.retrofit.model

import com.google.gson.annotations.SerializedName

data class PlaylistSongs(
    @SerializedName("Song") val song: SongDto,
)
