package com.kma.musicplayerv2.network.retrofit.repository

import com.kma.musicplayerv2.globalstate.CurrentUser
import com.kma.musicplayerv2.model.SongComment
import com.kma.musicplayerv2.network.common.ApiCallback
import com.kma.musicplayerv2.network.retrofit.RetrofitClient
import com.kma.musicplayerv2.network.retrofit.api.CommentApi
import com.kma.musicplayerv2.network.retrofit.model.AddCommentRequest
import com.kma.musicplayerv2.network.retrofit.model.AddCommentResponse
import com.kma.musicplayerv2.network.retrofit.model.GetCommentResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object CommentRepository {
    private val commentApi: CommentApi = RetrofitClient.getClient().create(CommentApi::class.java)
    fun addComment(songId: String, content: String, apiCallback: ApiCallback<SongComment>) {
        commentApi.addComment(AddCommentRequest(CurrentUser.getUser()!!.id, songId, content)).enqueue(
            object : Callback<AddCommentResponse> {
                override fun onResponse(call: Call<AddCommentResponse>, response: Response<AddCommentResponse>) {
                    if (response.body() != null) {
                        apiCallback.onSuccess(response.body()!!.data.toSongComment(CurrentUser.getUser()!!))
                    } else {
                        apiCallback.onFailure("Unknown error")
                    }
                }

                override fun onFailure(call: Call<AddCommentResponse>, t: Throwable) {
                    apiCallback.onFailure(t.message ?: "Unknown error")
                }
            }
        )
    }

    fun getCommentBySongId(songId: String, apiCallback: ApiCallback<List<SongComment>>) {
        commentApi.getCommentBySongId(songId).enqueue(
            object : Callback<GetCommentResponse> {
                override fun onResponse(call: Call<GetCommentResponse>, response: Response<GetCommentResponse>) {
                    if (response.body() != null) {
                        apiCallback.onSuccess(response.body()!!.data.map { it.toSongComment() })
                    } else {
                        apiCallback.onFailure("Unknown error")
                    }
                }

                override fun onFailure(call: Call<GetCommentResponse>, t: Throwable) {
                    apiCallback.onFailure(t.message ?: "Unknown error")
                }
            }
        )
    }
}