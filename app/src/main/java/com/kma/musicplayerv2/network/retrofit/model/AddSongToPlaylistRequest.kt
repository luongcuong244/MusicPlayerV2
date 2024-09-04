package com.kma.musicplayerv2.network.retrofit.model

import com.google.gson.annotations.SerializedName

data class AddSongToPlaylistRequest (
    @SerializedName("songId") val songId: Long,
    @SerializedName("playlistId") val playlistId: Long
)