package com.kma.musicplayerv2.network.retrofit.api

import com.kma.musicplayerv2.network.retrofit.model.AddSongToPlaylistRequest
import com.kma.musicplayerv2.network.retrofit.model.DeleteSongToPlaylistRequest
import com.kma.musicplayerv2.network.retrofit.model.PlaylistDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PlaylistApi {
    @GET("app/get-all-playlist")
    fun getAllPlaylists(): Call<List<PlaylistDto>>

    @POST("app/create-playlist")
    fun createPlaylist(playlistName: String): Call<PlaylistDto>

    @POST("app/add-song-to-playlist")
    fun addSongToPlaylist(@Body request: AddSongToPlaylistRequest): Call<Boolean>

    @POST()
    fun removeSongFromPlaylist(@Body request: DeleteSongToPlaylistRequest): Call<Void>

    @POST("get-recently-playlist")
    fun getRecentlyPlaylist(): Call<List<PlaylistDto>>
}