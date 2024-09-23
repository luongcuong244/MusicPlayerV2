package com.kma.musicplayerv2.network.retrofit.model

data class ExploreResponse(
    val playlists: List<PlaylistDto>,
    val recentlyPlaylists: List<PlaylistDto>,
)
