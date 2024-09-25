package com.kma.musicplayerv2.network.retrofit.api

import com.kma.musicplayerv2.network.retrofit.model.AddCommentRequest
import com.kma.musicplayerv2.network.retrofit.model.GetAllSongResponse
import com.kma.musicplayerv2.network.retrofit.model.GetFavouriteSongResponse
import com.kma.musicplayerv2.network.retrofit.model.GetHiddenSongResponse
import com.kma.musicplayerv2.network.retrofit.model.HideSongRequest
import com.kma.musicplayerv2.network.retrofit.model.HideSongResponse
import com.kma.musicplayerv2.network.retrofit.model.SongCommentDto
import com.kma.musicplayerv2.network.retrofit.model.SongDto
import com.kma.musicplayerv2.network.retrofit.model.UnhideSongRequest
import com.kma.musicplayerv2.network.retrofit.model.UnhideSongResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SongApi {
    @GET("/api/song/v1/get-all")
    fun getAllSongs(): Call<GetAllSongResponse>

    @GET("/api/song/v1/get-favourite-songs")
    fun getFavouriteSongs(@Query("userId") userId: String): Call<GetFavouriteSongResponse>

    @GET("app/get-uploaded-songs")
    fun getUploadedSongs(): Call<List<SongDto>>

    @POST("app/add-favourite-song")
    fun addFavouriteSong(songId: String): Call<Void>

    @POST("app/remove-favourite-song")
    fun removeFavouriteSong(songId: String): Call<Void>

    @GET("/api/song/v1/get-hidden-songs")
    fun getHiddenSongs(@Query("userId") userId: String): Call<GetHiddenSongResponse>

    @POST("/api/song/v1/add-hide-song")
    fun hideSong(@Body hideSongRequest: HideSongRequest): Call<HideSongResponse>

    @POST("/api/song/v1/unhide-songs")
    fun unhideSongs(@Body request: UnhideSongRequest): Call<UnhideSongResponse>

    @GET
    fun getSongsById(songIds: List<String>): Call<List<SongDto>>

    @GET
    fun getCommentsBySongId(songId: String): Call<List<SongCommentDto>>

    @POST("app/add-comment")
    fun addComment(@Body request: AddCommentRequest): Call<SongCommentDto>

    @GET
    fun getSongsByPlaylistId(playlistId: String): Call<List<SongDto>>

    @GET
    fun getRecentlySongs(): Call<List<SongDto>>
}