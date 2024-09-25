package com.kma.musicplayerv2.network.retrofit.api

import com.kma.musicplayerv2.network.retrofit.model.AddSongToPlaylistRequest
import com.kma.musicplayerv2.network.retrofit.model.AddSongToPlaylistResponse
import com.kma.musicplayerv2.network.retrofit.model.CreatePlaylistRequest
import com.kma.musicplayerv2.network.retrofit.model.CreatePlaylistResponse
import com.kma.musicplayerv2.network.retrofit.model.DeletePlaylistResponse
import com.kma.musicplayerv2.network.retrofit.model.DeleteSongToPlaylistRequest
import com.kma.musicplayerv2.network.retrofit.model.GetAllPlaylistResponse
import com.kma.musicplayerv2.network.retrofit.model.PlaylistDto
import com.kma.musicplayerv2.network.retrofit.model.RemoveSongFromPlaylistResponse
import com.kma.musicplayerv2.network.retrofit.model.TriggerRecentlyPlaylistResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PlaylistApi {
    @GET("/api/play-list/v1/get-all")
    fun getAllPlaylists(): Call<GetAllPlaylistResponse>

    @POST("/api/play-list/v1")
    fun createPlaylist(@Body request: CreatePlaylistRequest): Call<CreatePlaylistResponse>

    @POST("/api/play-list/v1/add-songs")
    fun addSongToPlaylist(@Body request: AddSongToPlaylistRequest): Call<AddSongToPlaylistResponse>

    @POST("/api/play-list/v1/remove-songs")
    fun removeSongFromPlaylist(@Body request: DeleteSongToPlaylistRequest): Call<RemoveSongFromPlaylistResponse>

    @POST("/api/play-list/v1/{playlistId}")
    fun triggerRecentlyPlaylist(@Path("playlistId") playlistId: String, @Body name: Map<String, String>): Call<TriggerRecentlyPlaylistResponse>

    @DELETE("/api/play-list/v1/{playlistId}")
    fun deletePlaylist(@Path("playlistId") playlistId: String): Call<DeletePlaylistResponse>
}