package com.kma.musicplayerv2.ui.screen.downloadedsong

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.databinding.ActivityDownloadedSongBinding
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

class DownloadedSongActivity : BaseActivity<ActivityDownloadedSongBinding>() {

    private lateinit var downloadedSongViewModel: DownloadedSongViewModel

    private var songAdapter: SongAdapter? = null

    override fun getContentView(): Int = R.layout.activity_downloaded_song

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        downloadedSongViewModel = ViewModelProvider(this)[DownloadedSongViewModel::class.java]
        downloadedSongViewModel.fetchDownloadedSongs(this, onSuccessful = {
            setupSongAdapter()
        })
        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.llSort.setOnClickListener {
            val bottomSheet = SortByBottomSheet(
                sortBy = downloadedSongViewModel.sortBy.value!!,
                onSortBy = {
                    downloadedSongViewModel.setSortBy(it)
                }
            )
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }
        binding.llFilter.setOnClickListener {
            val filterByArtistBottomSheet = FilterByArtistBottomSheet(
                songs = downloadedSongViewModel.songs,
                filterByArtists = downloadedSongViewModel.filterByArtists,
                onClickApply = {
                    downloadedSongViewModel.setFilterByArtists(it)
                    songAdapter?.notifyDataSetChanged()
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
                        Random.nextInt(downloadedSongViewModel.tempSongs.size)
                    )
                    putSerializable(
                        Constant.BUNDLE_SONGS,
                        downloadedSongViewModel.tempSongs as Serializable
                    )
                },
            )
        }
    }

    private fun setupObservers() {
        downloadedSongViewModel.sortBy.observe(this) {
            binding.tvSort.text = getString(it.textId)
            downloadedSongViewModel.sortSongs()
            songAdapter?.notifyDataSetChanged()
        }
        downloadedSongViewModel.totalSongs.observe(this) {
            binding.tvTotalSong.text =
                "${downloadedSongViewModel.tempSongs.size} ${getString(R.string.song)}"
        }
    }

    private fun setupSongAdapter() {
        songAdapter = SongAdapter(
            songs = downloadedSongViewModel.tempSongs,
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
                                songAdapter?.notifyDataSetChanged()
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
                    onClickAddToFavorite = { song, isFavourite ->
                        if (isFavourite) {
                            downloadedSongViewModel.unFavouriteSong(
                                context = this,
                                song = song,
                                onUnFavouriteSuccess = {
                                    songAdapter?.notifyDataSetChanged()
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
                                                    this@DownloadedSongActivity,
                                                    "Thêm bài hát vào playlist thành công",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                playlist.songs.add(song)
                                            } else {
                                                Toast.makeText(
                                                    this@DownloadedSongActivity,
                                                    "Failed to add song to playlist",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }

                                        override fun onFailure(message: String) {
                                            Toast.makeText(
                                                this@DownloadedSongActivity,
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
                        downloadedSongViewModel.hideSong(
                            context = this,
                            song = it,
                            onHideSuccess = {
                                songAdapter?.notifyDataSetChanged()

                                if (downloadedSongViewModel.tempSongs.isEmpty()) {
                                    finish()
                                }
                            }
                        )
                    }
                )
                bottomSheet.show(supportFragmentManager, bottomSheet.tag)
            },
            onClickItem = {
                showActivity(
                    PlaySongActivity::class.java,
                    Bundle().apply {
                        putInt(
                            Constant.BUNDLE_START_FROM_INDEX,
                            downloadedSongViewModel.tempSongs.indexOf(it)
                        )
                        putSerializable(
                            Constant.BUNDLE_SONGS,
                            downloadedSongViewModel.tempSongs as Serializable
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