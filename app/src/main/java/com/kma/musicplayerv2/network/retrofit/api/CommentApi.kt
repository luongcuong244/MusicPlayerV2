package com.kma.musicplayerv2.network.retrofit.api

import com.kma.musicplayerv2.network.retrofit.model.AddCommentRequest
import com.kma.musicplayerv2.network.retrofit.model.AddCommentResponse
import com.kma.musicplayerv2.network.retrofit.model.GetCommentResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CommentApi {
    @POST("/api/comment/v1")
    fun addComment(@Body request: AddCommentRequest): Call<AddCommentResponse>

    @GET("/api/comment/v1/{songId}")
    fun getCommentBySongId(@Path("songId") songId: String): Call<GetCommentResponse>
}