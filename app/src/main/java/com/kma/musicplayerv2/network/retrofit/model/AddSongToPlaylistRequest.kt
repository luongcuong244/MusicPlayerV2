package com.kma.musicplayerv2.network.retrofit.model

import com.google.gson.annotations.SerializedName

data class AddSongToPlaylistRequest (
    @SerializedName("songIds") val songIds: List<String>,
    @SerializedName("playlistId") val playlistId: String
)