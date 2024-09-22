package com.kma.musicplayerv2.network.retrofit.api

import com.kma.musicplayerv2.network.retrofit.model.AddSongToPlaylistRequest
import com.kma.musicplayerv2.network.retrofit.model.CreatePlaylistRequest
import com.kma.musicplayerv2.network.retrofit.model.CreatePlaylistResponse
import com.kma.musicplayerv2.network.retrofit.model.DeleteSongToPlaylistRequest
import com.kma.musicplayerv2.network.retrofit.model.GetAllPlaylistResponse
import com.kma.musicplayerv2.network.retrofit.model.PlaylistDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PlaylistApi {
    @GET("/api/play-list/v1/get-all")
    fun getAllPlaylists(): Call<GetAllPlaylistResponse>

    @POST("/api/play-list/v1")
    fun createPlaylist(@Body request: CreatePlaylistRequest): Call<CreatePlaylistResponse>

    @POST("app/add-song-to-playlist")
    fun addSongToPlaylist(@Body request: AddSongToPlaylistRequest): Call<Boolean>

    @POST()
    fun removeSongFromPlaylist(@Body request: DeleteSongToPlaylistRequest): Call<Void>

    @POST("get-recently-playlist")
    fun getRecentlyPlaylist(): Call<List<PlaylistDto>>
}