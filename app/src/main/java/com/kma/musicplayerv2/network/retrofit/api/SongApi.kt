package com.kma.musicplayer.network.retrofit.api

import com.kma.musicplayerv2.network.retrofit.model.AddCommentRequest
import com.kma.musicplayerv2.network.retrofit.model.SongCommentDto
import com.kma.musicplayerv2.network.retrofit.model.SongDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SongApi {
    @GET("app/get-all-songs")
    fun getAllSongs(): Call<List<SongDto>>

    @GET("app/get-favourite-songs")
    fun getFavouriteSongs(): Call<List<SongDto>>

    @GET("app/get-uploaded-songs")
    fun getUploadedSongs(): Call<List<SongDto>>

    @POST("app/add-favourite-song")
    fun addFavouriteSong(songId: Long): Call<Void>

    @POST("app/remove-favourite-song")
    fun removeFavouriteSong(songId: Long): Call<Void>

    @POST("app/hide-song")
    fun hideSong(songId: Long): Call<Void>

    @GET
    fun getSongsById(songIds: List<Long>): Call<List<SongDto>>

    @GET
    fun getCommentsBySongId(songId: Long): Call<List<SongCommentDto>>

    @POST("app/add-comment")
    fun addComment(@Body request: AddCommentRequest): Call<SongCommentDto>
}