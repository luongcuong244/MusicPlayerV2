package com.kma.musicplayerv2.network.retrofit.repository

import com.kma.musicplayerv2.model.Playlist
import com.kma.musicplayerv2.network.common.ApiCallback
import com.kma.musicplayerv2.network.retrofit.RetrofitClient
import com.kma.musicplayerv2.network.retrofit.api.PlaylistApi
import com.kma.musicplayerv2.network.retrofit.model.AddSongToPlaylistRequest
import com.kma.musicplayerv2.network.retrofit.model.PlaylistDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object PlaylistRepository {
    private val playlistApi: PlaylistApi = RetrofitClient.getClient().create(PlaylistApi::class.java)

    fun getAllPlaylists(apiCallback: ApiCallback<List<Playlist>>) {
        apiCallback.onSuccess(
            listOf(
                Playlist(
                    id = 1,
                    totalSong = 10,
                    name = "Playlist 1",
                    image = "https://images.pexels.com/photos/4162581/pexels-photo-4162581.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
                ),
                Playlist(
                    id = 1,
                    totalSong = 15,
                    name = "#Playlist 2",
                    image = "https://images.pexels.com/photos/5699509/pexels-photo-5699509.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
                ),
                Playlist(
                    id = 1,
                    totalSong = 20,
                    name = "Hay nghe",
                    image = "https://images.pexels.com/photos/5965930/pexels-photo-5965930.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
                ),
            )
        )
        return
        playlistApi.getAllPlaylists().enqueue(
            object : Callback<List<PlaylistDto>> {
                override fun onResponse(
                    call: Call<List<PlaylistDto>>,
                    response: Response<List<PlaylistDto>>
                ) {
                    val playlists = response.body()?.map { it.toPlaylist() }
                    if (playlists != null) {
                        apiCallback.onSuccess(playlists)
                    } else {
                        apiCallback.onFailure("Unknown error")
                    }
                }

                override fun onFailure(call: Call<List<PlaylistDto>>, t: Throwable) {
                    apiCallback.onFailure(t.message ?: "Unknown error")
                }
            }
        )
    }

    fun addSongToPlaylist(songId: Long, playlistId: Long, apiCallback: ApiCallback<Boolean>) {
        apiCallback.onSuccess(true)
        return
        playlistApi.addSongToPlaylist(AddSongToPlaylistRequest(songId, playlistId)).enqueue(
            object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if (response.isSuccessful) {
                        apiCallback.onSuccess(response.body() ?: false)
                    } else {
                        apiCallback.onFailure("Unknown error")
                    }
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    apiCallback.onFailure(t.message ?: "Unknown error")
                }
            }
        )
    }
}