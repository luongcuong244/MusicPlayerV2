package com.kma.musicplayerv2.ui.screen.listensongrecently

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kma.musicplayerv2.ui.core.BaseFragment
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.databinding.FragmentListenSongRecentlyBinding
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

class ListenSongRecentlyFragment : BaseFragment<FragmentListenSongRecentlyBinding>() {

    private lateinit var listenSongRecentlyViewModel: ListenSongRecentlyViewModel
    private var songAdapter: SongAdapter? = null


    override fun getContentView(): Int = R.layout.fragment_listen_song_recently

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenSongRecentlyViewModel = ViewModelProvider(this)[ListenSongRecentlyViewModel::class.java]
        listenSongRecentlyViewModel.fetchRecentlySongs(requireActivity(), onSuccessful = {
            setupSongAdapter()
        })
        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.llSort.setOnClickListener {
            val bottomSheet = SortByBottomSheet(
                sortBy = listenSongRecentlyViewModel.sortBy.value!!,
                onSortBy = {
                    listenSongRecentlyViewModel.setSortBy(it)
                }
            )
            bottomSheet.show(childFragmentManager, bottomSheet.tag)
        }
        binding.llFilter.setOnClickListener {
            val filterByArtistBottomSheet = FilterByArtistBottomSheet(
                songs = listenSongRecentlyViewModel.songs,
                filterByArtists = listenSongRecentlyViewModel.filterByArtists,
                onClickApply = {
                    listenSongRecentlyViewModel.setFilterByArtists(it)
                    songAdapter?.notifyDataSetChanged()
                }
            )
            filterByArtistBottomSheet.show(childFragmentManager, filterByArtistBottomSheet.tag)
        }
        binding.rlPlayRandomly.setOnClickListener {
            showActivity(
                PlaySongActivity::class.java,
                Bundle().apply {
                    putInt(
                        Constant.BUNDLE_START_FROM_INDEX,
                        Random.nextInt(listenSongRecentlyViewModel.tempSongs.size)
                    )
                    putSerializable(
                        Constant.BUNDLE_SONGS,
                        listenSongRecentlyViewModel.tempSongs as Serializable
                    )
                },
            )
        }
    }

    private fun setupObservers() {
        listenSongRecentlyViewModel.sortBy.observe(requireActivity()) {
            binding.tvSort.text = getString(it.textId)
            listenSongRecentlyViewModel.sortSongs()
            songAdapter?.notifyDataSetChanged()
        }
    }

    private fun setupSongAdapter() {
        songAdapter = SongAdapter(
            songs = listenSongRecentlyViewModel.tempSongs,
            onClickMore = { song ->
                val bottomSheet = SongOptionBottomSheet(
                    song = song,
                    onClickShare = {
                        ShareUtils.shareSong(requireActivity(), it)
                    },
                    onClickDownload = {
                        SongDownloader.downloadSong(
                            context = requireActivity(),
                            song = song,
                            onDownloadSuccess = {
                                song.isDownloaded = true
                                songAdapter?.notifyDataSetChanged()
                                Toast.makeText(
                                    requireActivity(),
                                    getString(R.string.download_successfully),
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onDownloadFailed = {
                                Toast.makeText(
                                    requireActivity(),
                                    getString(R.string.download_failed),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    },
                    onClickAddToFavorite = { song, isFavourite ->
                        if (isFavourite) {
                            listenSongRecentlyViewModel.unFavouriteSong(
                                context = requireActivity(),
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
                                                    requireActivity(),
                                                    "Thêm bài hát vào playlist thành công",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                playlist.songs.add(song)
                                            } else {
                                                Toast.makeText(
                                                    requireActivity(),
                                                    "Failed to add song to playlist",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }

                                        override fun onFailure(message: String) {
                                            Toast.makeText(
                                                requireActivity(),
                                                "Failed to add song to playlist: $message",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                )
                            }
                        )
                        addToPlaylistBottomSheet.show(
                            childFragmentManager,
                            addToPlaylistBottomSheet.tag
                        )
                    },
                    onClickPlayNext = {
                        (requireActivity() as BaseActivity<*>).songService?.addSongToNextPlay(it)
                        Toast.makeText(
                            requireActivity(),
                            getString(R.string.added_to_next_play),
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onClickHideSong = {
                        listenSongRecentlyViewModel.hideSong(
                            context = requireActivity(),
                            song = it,
                            onHideSuccess = {
                                songAdapter?.notifyDataSetChanged()
                            }
                        )
                    }
                )
                bottomSheet.show(childFragmentManager, bottomSheet.tag)
            },
            onClickItem = {
                showActivity(
                    PlaySongActivity::class.java,
                    Bundle().apply {
                        putInt(
                            Constant.BUNDLE_START_FROM_INDEX,
                            listenSongRecentlyViewModel.tempSongs.indexOf(it)
                        )
                        putSerializable(
                            Constant.BUNDLE_SONGS,
                            listenSongRecentlyViewModel.tempSongs as Serializable
                        )
                    },
                )
            }
        )
        binding.rvSong.apply {
            adapter = songAdapter
        }
        binding.rvSong.addItemDecoration(
            VerticalSpaceItemDecoration(
                this.resources.getDimension(
                    com.intuit.sdp.R.dimen._13sdp
                ).toInt()
            )
        )
        binding.rvSong.layoutManager = LinearLayoutManager(requireActivity())
    }
}