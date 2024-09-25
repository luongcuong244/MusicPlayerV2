package com.kma.musicplayerv2.network.retrofit.repository

import com.kma.musicplayerv2.globalstate.CurrentUser
import com.kma.musicplayerv2.model.Playlist
import com.kma.musicplayerv2.network.common.ApiCallback
import com.kma.musicplayerv2.network.retrofit.RetrofitClient
import com.kma.musicplayerv2.network.retrofit.api.PlaylistApi
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
import retrofit2.Callback
import retrofit2.Response

object PlaylistRepository {
    private val playlistApi: PlaylistApi = RetrofitClient.getClient().create(PlaylistApi::class.java)

    fun getAllPlaylists(apiCallback: ApiCallback<List<Playlist>>) {
//        apiCallback.onSuccess(
//            listOf(
//                Playlist(
//                    id = 1,
//                    totalSong = 10,
//                    name = "Playlist 1",
//                    image = "https://images.pexels.com/photos/4162581/pexels-photo-4162581.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
//                ),
//                Playlist(
//                    id = 1,
//                    totalSong = 15,
//                    name = "#Playlist 2",
//                    image = "https://images.pexels.com/photos/5699509/pexels-photo-5699509.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
//                ),
//                Playlist(
//                    id = 1,
//                    totalSong = 20,
//                    name = "Hay nghe",
//                    image = "https://images.pexels.com/photos/5965930/pexels-photo-5965930.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
//                ),
//            )
//        )
//        return
        playlistApi.getAllPlaylists().enqueue(
            object : Callback<GetAllPlaylistResponse> {
                override fun onResponse(
                    call: Call<GetAllPlaylistResponse>,
                    response: Response<GetAllPlaylistResponse>
                ) {
                    val playlists = response.body()?.data?.map { it.toPlaylist() }
                    if (playlists != null) {
                        apiCallback.onSuccess(playlists)
                    } else {
                        apiCallback.onFailure("Unknown error")
                    }
                }

                override fun onFailure(call: Call<GetAllPlaylistResponse>, t: Throwable) {
                    apiCallback.onFailure(t.message ?: "Unknown error")
                }
            }
        )
    }

    fun addSongToPlaylist(songId: String, playlistId: String, apiCallback: ApiCallback<Boolean>) {
        playlistApi.addSongToPlaylist(AddSongToPlaylistRequest(listOf(songId), playlistId)).enqueue(
            object : Callback<AddSongToPlaylistResponse> {
                override fun onResponse(call: Call<AddSongToPlaylistResponse>, response: Response<AddSongToPlaylistResponse>) {
                    if (response.isSuccessful) {
                        apiCallback.onSuccess(true)
                    } else {
                        apiCallback.onSuccess(false)
                    }
                }

                override fun onFailure(call: Call<AddSongToPlaylistResponse>, t: Throwable) {
                    apiCallback.onFailure(t.message ?: "Unknown error")
                }
            }
        )
    }

    fun removeSongFromPlaylist(songId: String, playlistId: String, apiCallback: ApiCallback<Void>) {
        playlistApi.removeSongFromPlaylist(DeleteSongToPlaylistRequest(listOf(songId), playlistId)).enqueue(
            object : Callback<RemoveSongFromPlaylistResponse> {
                override fun onResponse(call: Call<RemoveSongFromPlaylistResponse>, response: Response<RemoveSongFromPlaylistResponse>) {
                    if (response.isSuccessful) {
                        apiCallback.onSuccess(null)
                    } else {
                        apiCallback.onFailure("Unknown error")
                    }
                }

                override fun onFailure(call: Call<RemoveSongFromPlaylistResponse>, t: Throwable) {
                    apiCallback.onFailure(t.message ?: "Unknown error")
                }
            }
        )
    }

    fun getRecentlyPlaylist(apiCallback: ApiCallback<List<Playlist>>) {
        getAllPlaylists(
            object : ApiCallback<List<Playlist>> {
                override fun onSuccess(data: List<Playlist>?) {
                    if (data.isNullOrEmpty()) {
                        apiCallback.onSuccess(emptyList())
                        return
                    }
                    // lấy ra playlist có updated_at 7 ngày gần nhất
                    val sevenDaysAgo = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000
                    val playlist = data.filter { it.updatedAt >= sevenDaysAgo }
                    apiCallback.onSuccess(playlist)
                }

                override fun onFailure(message: String) {
                    apiCallback.onFailure(message)
                }
            }
        )
    }

    fun createPlaylist(name: String, apiCallback: ApiCallback<Playlist>) {
//        apiCallback.onSuccess(
//            Playlist(
//                id = 1,
//                totalSong = 0,
//                name = name,
//                image = "https://images.pexels.com/photos/4162581/pexels-photo-4162581.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
//            )
//        )
//        return
        playlistApi.createPlaylist(
            CreatePlaylistRequest(name, CurrentUser.getUser()!!.id)
        ).enqueue(
            object : Callback<CreatePlaylistResponse> {
                override fun onResponse(call: Call<CreatePlaylistResponse>, response: Response<CreatePlaylistResponse>) {
                    val playlist = response.body()?.playlist?.toPlaylist()
                    if (playlist != null) {
                        apiCallback.onSuccess(playlist)
                    } else {
                        apiCallback.onFailure("Unknown error")
                    }
                }

                override fun onFailure(call: Call<CreatePlaylistResponse>, t: Throwable) {
                    apiCallback.onFailure(t.message ?: "Unknown error")
                }
            }
        )
    }

    fun triggerRecentlyPlaylist(playlistId: String, playlistName: String) {
        val map = mapOf(
            "name" to playlistName
        )
        playlistApi.triggerRecentlyPlaylist(playlistId, map).enqueue(
            object : Callback<TriggerRecentlyPlaylistResponse> {
                override fun onResponse(call: Call<TriggerRecentlyPlaylistResponse>, response: Response<TriggerRecentlyPlaylistResponse>) {

                }

                override fun onFailure(call: Call<TriggerRecentlyPlaylistResponse>, t: Throwable) {

                }
            }
        )
    }

    fun deletePlaylist(playlistId: String, apiCallback: ApiCallback<Void>) {
        playlistApi.deletePlaylist(playlistId).enqueue(
            object : Callback<DeletePlaylistResponse> {
                override fun onResponse(call: Call<DeletePlaylistResponse>, response: Response<DeletePlaylistResponse>) {
                    if (response.isSuccessful) {
                        apiCallback.onSuccess(null)
                    } else {
                        apiCallback.onFailure("Unknown error")
                    }
                }

                override fun onFailure(call: Call<DeletePlaylistResponse>, t: Throwable) {
                    apiCallback.onFailure(t.message ?: "Unknown error")
                }
            }
        )
    }
}