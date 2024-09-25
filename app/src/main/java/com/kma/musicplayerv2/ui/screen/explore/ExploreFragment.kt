package com.kma.musicplayerv2.ui.screen.explore

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.kma.musicplayerv2.ui.core.BaseFragment
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.databinding.FragmentExploreBinding
import com.kma.musicplayerv2.model.Playlist
import com.kma.musicplayerv2.model.Song
import com.kma.musicplayerv2.network.common.ApiCallback
import com.kma.musicplayerv2.network.retrofit.model.ExploreResponse
import com.kma.musicplayerv2.network.retrofit.model.SongDto
import com.kma.musicplayerv2.network.retrofit.repository.ExploreRepository
import com.kma.musicplayerv2.network.retrofit.repository.PlaylistRepository
import com.kma.musicplayerv2.network.retrofit.repository.SongRepository
import com.kma.musicplayerv2.ui.adapter.ListenRecentlyAdapter
import com.kma.musicplayerv2.ui.adapter.SongAdapter
import com.kma.musicplayerv2.ui.bottomsheet.AddToPlaylistBottomSheet
import com.kma.musicplayerv2.ui.bottomsheet.SongOptionBottomSheet
import com.kma.musicplayerv2.ui.core.BaseActivity
import com.kma.musicplayerv2.ui.customview.HorizontalSpaceItemDecoration
import com.kma.musicplayerv2.ui.customview.VerticalSpaceItemDecoration
import com.kma.musicplayerv2.ui.screen.listenrecently.ListenRecentlyActivity
import com.kma.musicplayerv2.ui.screen.playsong.PlaySongActivity
import com.kma.musicplayerv2.ui.screen.viewplaylist.ViewPlaylistActivity
import com.kma.musicplayerv2.utils.Constant
import com.kma.musicplayerv2.utils.ShareUtils
import com.kma.musicplayerv2.utils.SongDownloader
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class ExploreFragment : BaseFragment<FragmentExploreBinding>() {
    private var listenRecentlyAdapter: ListenRecentlyAdapter? = null
    private val recentlyPlaylists = mutableListOf<Playlist>()

    private var songForYouAdapter: SongAdapter? = null
    private val songForYou = mutableListOf<Song>()

    override fun getContentView(): Int = R.layout.fragment_explore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListenRecentlyAdapter()
        ExploreRepository.fetchExplore(
            object : Callback<ExploreResponse> {
                override fun onResponse(
                    call: Call<ExploreResponse>,
                    response: Response<ExploreResponse>
                ) {
                    if (response.isSuccessful) {
                        val songForYou = response.body()?.data?.songForYou
                        if (songForYou != null) {
                            this@ExploreFragment.songForYou.clear()
                            this@ExploreFragment.songForYou.addAll(songForYou.map { it.toSong() })
                            setupSongForYouAdapter()
                        }
                    }
                }

                override fun onFailure(call: Call<ExploreResponse>, t: Throwable) {

                }
            }
        )
    }

    private fun setupSongForYouAdapter() {
        songForYouAdapter = SongAdapter(
            songs = songForYou,
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
                                songForYouAdapter?.notifyDataSetChanged()
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
                            SongRepository.removeFavouriteSong(
                                song,
                                object : ApiCallback<Void> {
                                    override fun onSuccess(data: Void?) {

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
                        } else {
                            SongRepository.addFavouriteSong(
                                song,
                                object : ApiCallback<Void> {
                                    override fun onSuccess(data: Void?) {

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
                            requireActivity().supportFragmentManager,
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
                        SongRepository.hideSong(
                            song,
                            object : ApiCallback<Void> {
                                override fun onSuccess(data: Void?) {
                                    songForYou.remove(song)
                                    songForYouAdapter?.notifyDataSetChanged()
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
                )
                bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
            },
            onClickItem = {
                showActivity(
                    PlaySongActivity::class.java,
                    Bundle().apply {
                        putSerializable(
                            Constant.BUNDLE_SONG,
                            it as Serializable
                        )
                    },
                )
            }
        )
        binding.rvSongForYou.apply {
            adapter = songForYouAdapter
        }
        if (binding.rvSongForYou.itemDecorationCount == 0) {
            binding.rvSongForYou.addItemDecoration(
                VerticalSpaceItemDecoration(
                    this.resources.getDimension(
                        com.intuit.sdp.R.dimen._13sdp
                    ).toInt()
                )
            )
        }
        binding.rvSongForYou.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun setupListenRecentlyAdapter() {
        PlaylistRepository.getRecentlyPlaylist(
            object : ApiCallback<List<Playlist>> {
                override fun onSuccess(data: List<Playlist>?) {
                    if (data == null) {
                        onFailure("Data is null")
                        return
                    }
                    recentlyPlaylists.clear()
                    recentlyPlaylists.addAll(data)
                    listenRecentlyAdapter = ListenRecentlyAdapter(
                        recentlyPlaylists,
                        {
                            showActivityForResult(ViewPlaylistActivity::class.java, 100, Bundle().apply {
                                putSerializable(Constant.BUNDLE_PLAYLIST, it)
                            })
                        },
                        {
                            showActivityForResult(ListenRecentlyActivity::class.java, 100)
                        }
                    )
                    binding.rvListenRecently.adapter = listenRecentlyAdapter
                    if (binding.rvListenRecently.itemDecorationCount == 0) {
                        binding.rvListenRecently.addItemDecoration(
                            HorizontalSpaceItemDecoration(
                                requireActivity().resources.getDimension(
                                    com.intuit.sdp.R.dimen._13sdp
                                ).toInt()
                            )
                        )
                    }
                    binding.rvListenRecently.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                }

                override fun onFailure(message: String) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}