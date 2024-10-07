package com.kma.musicplayerv2.ui.screen.library

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.kma.musicplayerv2.model.Playlist
import com.kma.musicplayerv2.model.Song
import com.kma.musicplayerv2.network.common.ApiCallback
import com.kma.musicplayerv2.network.retrofit.repository.PlaylistRepository
import com.kma.musicplayerv2.network.retrofit.repository.SongRepository

class LibraryViewModel : ViewModel() {
    val playlists = mutableListOf<Playlist>()
    val recentlyPlaylists = mutableListOf<Playlist>()

    fun fetchPlaylists(
        context: Context,
        onSuccess: () -> Unit,
    ) {
        PlaylistRepository.getAllPlaylists(
            object : ApiCallback<List<Playlist>> {
                override fun onSuccess(data: List<Playlist>?) {
                    if (data == null) {
                        onFailure("Data is null")
                        return
                    }
                    playlists.clear()
                    playlists.addAll(data)
                    onSuccess()
                }

                override fun onFailure(message: String) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    fun fetchRecentlyPlaylist(
        context: Context,
        onSuccess: () -> Unit,
    ) {
        PlaylistRepository.getRecentlyPlaylist(
            object : ApiCallback<List<Playlist>> {
                override fun onSuccess(data: List<Playlist>?) {
                    if (data == null) {
                        onFailure("Data is null")
                        return
                    }
                    recentlyPlaylists.clear()
                    recentlyPlaylists.addAll(data)
                    onSuccess()
                }

                override fun onFailure(message: String) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    fun fetchTotalOfFavoriteSongs(
        context: Context,
        onSuccess: (Int) -> Unit,
    ) {
        SongRepository.getFavouriteSongs(
            object : ApiCallback<List<Song>> {
                override fun onSuccess(data: List<Song>?) {
                    if (data == null) {
                        onFailure("Data is null")
                        return
                    }
                    onSuccess(data.size)
                }

                override fun onFailure(message: String) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    fun fetchUploadSongs(
        context: Context,
        onSuccess: (List<Song>) -> Unit,
    ) {
        SongRepository.getUploadSongs(
            object : ApiCallback<List<Song>> {
                override fun onSuccess(data: List<Song>?) {
                    if (data == null) {
                        onFailure("Data is null")
                        return
                    }
                    onSuccess(data)
                }

                override fun onFailure(message: String) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}