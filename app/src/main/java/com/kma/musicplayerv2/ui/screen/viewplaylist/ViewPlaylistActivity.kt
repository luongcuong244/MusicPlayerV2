package com.kma.musicplayerv2.ui.screen.viewplaylist

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.databinding.ActivityViewPlaylistBinding
import com.kma.musicplayerv2.model.Playlist
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

class ViewPlaylistActivity : BaseActivity<ActivityViewPlaylistBinding>() {
    private lateinit var viewPlaylistViewModel: ViewPlaylistViewModel
    private var songAdapter: SongAdapter? = null

    private lateinit var playlist: Playlist

    override fun getContentView(): Int = R.layout.activity_view_playlist

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewPlaylistViewModel = ViewModelProvider(this)[ViewPlaylistViewModel::class.java]
        initView()
        setupListeners()
        setupObservers()
        viewPlaylistViewModel.init(playlist.songs)
        setupSongAdapter()

        if (playlist.songs.isEmpty()) {
            binding.tvNoSong.visibility = View.VISIBLE
            binding.rvSong.visibility = View.GONE
        }
//        viewPlaylistViewModel.fetchSongsByPlaylist(this, playlist.id, onSuccessful = {
//            setupSongAdapter()
//        })
    }

    private fun initView() {
        playlist = intent.getSerializableExtra(Constant.BUNDLE_PLAYLIST) as Playlist

        binding.tvPlaylistName.text = playlist.name
        if (playlist.songs.isNotEmpty()) {
            binding.progressBar.visibility = View.VISIBLE
            Glide.with(binding.root.context)
                .load(playlist.songs[0].thumbnail)
                .listener(object : RequestListener<Drawable> {
                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.d("CHECK_BUG", e.toString())
                        return false
                    }
                })
                .into(binding.ivThumb)
        }
        else {
            binding.progressBar.visibility = View.GONE
            binding.ivThumb.setImageResource(R.drawable.empty_playlist)
            // add padding to image view
            val padding = binding.root.context.resources.getDimension(com.intuit.sdp.R.dimen._10sdp).toInt()
            binding.ivThumb.setPadding(padding, padding, padding, padding)
        }
    }

    private fun setupListeners() {
        if (playlist.songs.isEmpty()) return

        binding.llSort.setOnClickListener {
            val bottomSheet = SortByBottomSheet(
                sortBy = viewPlaylistViewModel.sortBy.value!!,
                onSortBy = {
                    viewPlaylistViewModel.setSortBy(it)
                }
            )
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }
        binding.llFilter.setOnClickListener {
            val filterByArtistBottomSheet = FilterByArtistBottomSheet(
                songs = viewPlaylistViewModel.songs,
                filterByArtists = viewPlaylistViewModel.filterByArtists,
                onClickApply = {
                    viewPlaylistViewModel.setFilterByArtists(it)
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
                        Random.nextInt(viewPlaylistViewModel.tempSongs.size)
                    )
                    putSerializable(
                        Constant.BUNDLE_SONGS,
                        viewPlaylistViewModel.tempSongs as Serializable
                    )
                },
            )
        }
    }

    private fun setupObservers() {
        viewPlaylistViewModel.sortBy.observe(this) {
            binding.tvSort.text = getString(it.textId)
            viewPlaylistViewModel.sortSongs()
            songAdapter?.notifyDataSetChanged()
        }
        viewPlaylistViewModel.totalSongs.observe(this) {
            binding.tvTotalSong.text =
                "${viewPlaylistViewModel.tempSongs.size} ${getString(R.string.song)}"
        }
    }

    private fun setupSongAdapter() {
        songAdapter = SongAdapter(
            songs = viewPlaylistViewModel.tempSongs,
            onClickMore = { song ->
                val bottomSheet = SongOptionBottomSheet(
                    song = song,
                    onClickShare = {
                        ShareUtils.shareSong(this, it)
                    },
                    onDeleteFromPlaylist = {
                        PlaylistRepository.removeSongFromPlaylist(
                            playlistId = playlist.id,
                            songId = it.id,
                            apiCallback = object : ApiCallback<Void> {
                                override fun onSuccess(data: Void?) {
                                    viewPlaylistViewModel.songs.remove(it)
                                    viewPlaylistViewModel.tempSongs.remove(it)
                                    songAdapter?.notifyDataSetChanged()
                                    Toast.makeText(
                                        this@ViewPlaylistActivity,
                                        getString(R.string.delete_successfully),
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    if (viewPlaylistViewModel.tempSongs.isEmpty()) {
                                        finish()
                                    }
                                }

                                override fun onFailure(message: String) {
                                    Toast.makeText(
                                        this@ViewPlaylistActivity,
                                        getString(R.string.delete_failed),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        )
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
                    onClickAddToFavorite = {
                        if (it.isFavourite) {
                            viewPlaylistViewModel.unFavouriteSong(
                                context = this,
                                song = it,
                                onUnFavouriteSuccess = {
                                    it.isFavourite = false
                                    songAdapter?.notifyDataSetChanged()
                                }
                            )
                        } else {
                            viewPlaylistViewModel.addFavoriteSong(
                                context = this,
                                song = it,
                                onAddFavoriteSuccess = {
                                    it.isFavourite = true
                                    songAdapter?.notifyDataSetChanged()
                                }
                            )
                        }
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
                        viewPlaylistViewModel.hideSong(
                            context = this,
                            song = it,
                            onHideSuccess = {
                                songAdapter?.notifyDataSetChanged()

                                if (viewPlaylistViewModel.tempSongs.isEmpty()) {
                                    finish()
                                }
                            }
                        )
                    }
                )
                bottomSheet.show(supportFragmentManager, bottomSheet.tag)
            },
            onClickUnFavourite = { song, position ->
                viewPlaylistViewModel.unFavouriteSong(
                    context = this,
                    song = song,
                    onUnFavouriteSuccess = {
                        song.isFavourite = false
                        songAdapter?.notifyDataSetChanged()
                    }
                )
                songAdapter?.notifyItemRemoved(position)
            },
            onClickItem = {
                showActivity(
                    PlaySongActivity::class.java,
                    Bundle().apply {
                        putInt(
                            Constant.BUNDLE_START_FROM_INDEX,
                            viewPlaylistViewModel.tempSongs.indexOf(it)
                        )
                        putSerializable(
                            Constant.BUNDLE_SONGS,
                            viewPlaylistViewModel.tempSongs as Serializable
                        )
                    },
                )
                PlaylistRepository.triggerRecentlyPlaylist(playlist.id, playlist.name)
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
        binding.rvSong.layoutManager = LinearLayoutManager(this)
    }
}