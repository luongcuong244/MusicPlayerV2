package com.kma.musicplayerv2.ui.screen.favouritesong

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.databinding.ActivityFavouriteSongBinding
import com.kma.musicplayerv2.network.common.ApiCallback
import com.kma.musicplayerv2.network.retrofit.repository.PlaylistRepository
import com.kma.musicplayerv2.ui.adapter.SongAdapter
import com.kma.musicplayerv2.ui.bottomsheet.AddToPlaylistBottomSheet
import com.kma.musicplayerv2.ui.bottomsheet.FilterByArtistBottomSheet
import com.kma.musicplayerv2.ui.bottomsheet.SongOptionBottomSheet
import com.kma.musicplayerv2.ui.bottomsheet.SortByBottomSheet
import com.kma.musicplayerv2.ui.core.BaseActivity
import com.kma.musicplayerv2.ui.customview.VerticalSpaceItemDecoration
import com.kma.musicplayerv2.ui.screen.playsong.PlaySongActivity
import com.kma.musicplayerv2.utils.Constant
import com.kma.musicplayerv2.utils.ShareUtils
import com.kma.musicplayerv2.utils.SongDownloader
import java.io.Serializable
import kotlin.random.Random

class FavouriteSongActivity : BaseActivity<ActivityFavouriteSongBinding>() {

    private lateinit var favouriteSongViewModel: FavouriteSongViewModel

    private lateinit var songAdapter: SongAdapter

    override fun getContentView(): Int = R.layout.activity_favourite_song

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favouriteSongViewModel = ViewModelProvider(this)[FavouriteSongViewModel::class.java]
        favouriteSongViewModel.fetchFavouriteSongs(this, onSuccessful = {
            setupSongAdapter()
        })
        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.llSort.setOnClickListener {
            val bottomSheet = SortByBottomSheet(
                sortBy = favouriteSongViewModel.sortBy.value!!,
                onSortBy = {
                    favouriteSongViewModel.setSortBy(it)
                }
            )
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }
        binding.llFilter.setOnClickListener {
            val filterByArtistBottomSheet = FilterByArtistBottomSheet(
                songs = favouriteSongViewModel.songs,
                filterByArtists = favouriteSongViewModel.filterByArtists,
                onClickApply = {
                    favouriteSongViewModel.setFilterByArtists(it)
                    songAdapter.notifyDataSetChanged()
                }
            )
            filterByArtistBottomSheet.show(supportFragmentManager, filterByArtistBottomSheet.tag)
        }
        binding.rlPlayRandomly.setOnClickListener {
            showActivity(
                PlaySongActivity::class.java,
                Bundle().apply {
                    putInt(
                        Constant.BUNDLE_START_FROM_INDEX,
                        Random.nextInt(favouriteSongViewModel.tempSongs.size)
                    )
                    putSerializable(
                        Constant.BUNDLE_SONGS,
                        favouriteSongViewModel.tempSongs as Serializable
                    )
                },
            )
        }
    }

    private fun setupObservers() {
        favouriteSongViewModel.sortBy.observe(this) {
            binding.tvSort.text = getString(it.textId)
            favouriteSongViewModel.sortSongs()
            songAdapter.notifyDataSetChanged()
        }
        favouriteSongViewModel.totalSongs.observe(this) {
            binding.tvTotalSong.text =
                "${favouriteSongViewModel.tempSongs.size} ${getString(R.string.song)}"
        }
    }

    private fun setupSongAdapter() {
        songAdapter = SongAdapter(
            songs = favouriteSongViewModel.tempSongs,
            onClickMore = { song ->
                val bottomSheet = SongOptionBottomSheet(
                    song = song,
                    onClickShare = {
                        ShareUtils.shareSong(this, it)
                    },
                    onClickDownload = {
                        SongDownloader.downloadSong(
                            context = this,
                            song = song,
                            onDownloadSuccess = {
                                song.isDownloaded = true
                                songAdapter.notifyDataSetChanged()
                                Toast.makeText(
                                    this,
                                    getString(R.string.download_successfully),
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onDownloadFailed = {
                                Toast.makeText(
                                    this,
                                    getString(R.string.download_failed),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    },
                    onClickAddToFavorite = {
                        if (it.isFavourite) {
                            favouriteSongViewModel.unFavouriteSong(
                                context = this,
                                song = it,
                                onUnFavouriteSuccess = {
                                    it.isFavourite = false
                                    songAdapter.notifyDataSetChanged()
                                }
                            )
                        }
                    },
                    onClickAddToPlaylist = {
                        val addToPlaylistBottomSheet = AddToPlaylistBottomSheet(
                            onClickPlaylist = { playlist ->
                                // Add song to playlist
                                PlaylistRepository.addSongToPlaylist(
                                    songId = song.id,
                                    playlistId = playlist.id,
                                    apiCallback = object : ApiCallback<Boolean> {
                                        override fun onSuccess(data: Boolean?) {
                                            if (data == null) {
                                                onFailure("Unknown error")
                                                return
                                            }
                                            if (data) {
                                                Toast.makeText(
                                                    this@FavouriteSongActivity,
                                                    "Thêm bài hát vào playlist thành công",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                playlist.songs.add(song)
                                            } else {
                                                Toast.makeText(
                                                    this@FavouriteSongActivity,
                                                    "Failed to add song to playlist",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }

                                        override fun onFailure(message: String) {
                                            Toast.makeText(
                                                this@FavouriteSongActivity,
                                                "Failed to add song to playlist: $message",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                )
                            }
                        )
                        addToPlaylistBottomSheet.show(
                            supportFragmentManager,
                            addToPlaylistBottomSheet.tag
                        )
                    },
                    onClickPlayNext = {
                        songService?.addSongToNextPlay(it)
                        Toast.makeText(
                            this,
                            getString(R.string.added_to_next_play),
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onClickHideSong = {
                        favouriteSongViewModel.hideSong(
                            context = this,
                            song = it,
                            onHideSuccess = {
                                songAdapter.notifyDataSetChanged()
                            }
                        )
                    }
                )
                bottomSheet.show(supportFragmentManager, bottomSheet.tag)
            },
            onClickUnFavourite = { song, position ->
                favouriteSongViewModel.unFavouriteSong(
                    context = this,
                    song = song,
                    onUnFavouriteSuccess = {
                        song.isFavourite = false
                        songAdapter.notifyDataSetChanged()
                    }
                )
                songAdapter.notifyItemRemoved(position)
            },
            onClickItem = {
                showActivity(
                    PlaySongActivity::class.java,
                    Bundle().apply {
                        putInt(
                            Constant.BUNDLE_START_FROM_INDEX,
                            favouriteSongViewModel.tempSongs.indexOf(it)
                        )
                        putSerializable(
                            Constant.BUNDLE_SONGS,
                            favouriteSongViewModel.tempSongs as Serializable
                        )
                    },
                )
            }
        )
        binding.rvFavouriteSong.apply {
            adapter = songAdapter
        }
        binding.rvFavouriteSong.addItemDecoration(
            VerticalSpaceItemDecoration(
                this.resources.getDimension(
                    com.intuit.sdp.R.dimen._13sdp
                ).toInt()
            )
        )
        binding.rvFavouriteSong.layoutManager = LinearLayoutManager(this)
    }
}