package com.kma.musicplayerv2.ui.screen.listensongrecently

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kma.musicplayerv2.model.Artist
import com.kma.musicplayerv2.model.Song
import com.kma.musicplayerv2.model.SortType
import com.kma.musicplayerv2.network.common.ApiCallback
import com.kma.musicplayerv2.network.retrofit.repository.SongRepository

class ListenSongRecentlyViewModel : ViewModel() {
    val songs = mutableListOf<Song>()
    val tempSongs = mutableListOf<Song>()
    val filterByArtists = mutableListOf<Artist>()

    private val _sortBy = MutableLiveData(SortType.NEWEST)
    val sortBy: LiveData<SortType> = _sortBy

    fun fetchRecentlySongs(context: Context, onSuccessful: () -> Unit) {
        SongRepository.getRecentlySongs(
            object : ApiCallback<List<Song>> {
                override fun onSuccess(data: List<Song>?) {
                    if (data == null) {
                        onFailure("Unknown error")
                        return
                    }
                    songs.clear()
                    songs.addAll(data)
                    tempSongs.clear()
                    tempSongs.addAll(data)
                    sortSongs()
                    onSuccessful()
                }

                override fun onFailure(message: String) {
                    Toast.makeText(
                        context,
                        "Failed to fetch favourite songs: $message",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }

    fun sortSongs() {
        when (_sortBy.value) {
            SortType.NEWEST -> tempSongs.sortByDescending { it.createdTime }
            SortType.OLDEST -> tempSongs.sortBy { it.createdTime }
            SortType.SONG_NAME -> tempSongs.sortBy { it.title }
            SortType.ARTIST_NAME -> tempSongs.sortBy { it.artist.name }
            null -> return
        }
    }

    fun setFilterByArtists(artists: List<Artist>) {
        filterByArtists.clear()
        filterByArtists.addAll(artists)
        tempSongs.clear()
        if (artists.isEmpty()) {
            tempSongs.addAll(songs)
        } else {
            tempSongs.addAll(songs.filter { song -> artists.any { it.id == song.artist.id } })
        }
        sortSongs()
    }

    fun unFavouriteSong(context: Context, song: Song, onUnFavouriteSuccess: () -> Unit) {
        // call api
        SongRepository.removeFavouriteSong(
            song,
            object : ApiCallback<Void> {
                override fun onSuccess(data: Void?) {
                    songs.remove(song)
                    tempSongs.remove(song)
                    onUnFavouriteSuccess()
                }

                override fun onFailure(message: String) {
                    Toast.makeText(
                        context,
                        "Failed to unfavourite song: $message",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }

    fun hideSong(context: Context, song: Song, onHideSuccess: () -> Unit) {
        SongRepository.hideSong(
            song,
            object : ApiCallback<Void> {
                override fun onSuccess(data: Void?) {
                    songs.remove(song)
                    tempSongs.remove(song)
                    onHideSuccess()
                }

                override fun onFailure(message: String) {
                    Toast.makeText(
                        context,
                        "Failed to hide song: $message",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }

    fun setSortBy(sortType: SortType) {
        _sortBy.value = sortType
    }
}