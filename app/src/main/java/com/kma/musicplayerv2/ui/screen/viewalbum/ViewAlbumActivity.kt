package com.kma.musicplayerv2.ui.screen.viewalbum

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.databinding.ActivityViewAlbumBinding
import com.kma.musicplayerv2.model.Album
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

class ViewAlbumActivity: BaseActivity<ActivityViewAlbumBinding>() {
    private lateinit var viewAlbumViewModel: ViewAlbumViewModel
    private var songAdapter: SongAdapter? = null

    private lateinit var album: Album

    override fun getContentView(): Int = R.layout.activity_view_album

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewAlbumViewModel = ViewModelProvider(this)[ViewAlbumViewModel::class.java]
        initView()
        setupListeners()
        setupObservers()
        viewAlbumViewModel.init(album.songs)
        setupSongAdapter()

        if (album.songs.isEmpty()) {
            binding.tvNoSong.visibility = View.VISIBLE
            binding.rvSong.visibility = View.GONE
        }
    }

    private fun initView() {
        album = intent.getSerializableExtra(Constant.BUNDLE_ALBUM) as Album

        binding.tvPlaylistName.text = album.title
        binding.progressBar.visibility = View.VISIBLE
        Glide.with(binding.root.context)
            .load(album.thumbnail)
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

    private fun setupListeners() {
//        binding.ivMore.setOnClickListener {
//            val playlistOptionBottomSheet = PlaylistOptionBottomSheet(
//                playlist = album,
//                onDeletePlaylist = {
//                    PlaylistRepository.deletePlaylist(
//                        playlistId = it.id,
//                        apiCallback = object : ApiCallback<Void> {
//                            override fun onSuccess(data: Void?) {
//                                Toast.makeText(
//                                    this@ViewPlaylistActivity,
//                                    getString(R.string.delete_successfully),
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                                finish()
//                            }
//
//                            override fun onFailure(message: String) {
//                                Toast.makeText(
//                                    this@ViewPlaylistActivity,
//                                    getString(R.string.delete_failed),
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                        }
//                    )
//                }
//            )
//            playlistOptionBottomSheet.show(supportFragmentManager, playlistOptionBottomSheet.tag)
//        }

        if (album.songs.isEmpty()) return

        binding.llSort.setOnClickListener {
            val bottomSheet = SortByBottomSheet(
                sortBy = viewAlbumViewModel.sortBy.value!!,
                onSortBy = {
                    viewAlbumViewModel.setSortBy(it)
                }
            )
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }
        binding.llFilter.setOnClickListener {
            val filterByArtistBottomSheet = FilterByArtistBottomSheet(
                songs = viewAlbumViewModel.songs,
                filterByArtists = viewAlbumViewModel.filterByArtists,
                onClickApply = {
                    viewAlbumViewModel.setFilterByArtists(it)
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
                        Random.nextInt(viewAlbumViewModel.tempSongs.size)
                    )
                    putSerializable(
                        Constant.BUNDLE_SONGS,
                        viewAlbumViewModel.tempSongs as Serializable
                    )
                },
            )
        }
    }

    private fun setupObservers() {
        viewAlbumViewModel.sortBy.observe(this) {
            binding.tvSort.text = getString(it.textId)
            viewAlbumViewModel.sortSongs()
            songAdapter?.notifyDataSetChanged()
        }
        viewAlbumViewModel.totalSongs.observe(this) {
            binding.tvTotalSong.text =
                "${viewAlbumViewModel.tempSongs.size} ${getString(R.string.song)}"
        }
    }

    private fun setupSongAdapter() {
        songAdapter = SongAdapter(
            songs = viewAlbumViewModel.tempSongs,
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
                            viewAlbumViewModel.unFavouriteSong(
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
                                                    this@ViewAlbumActivity,
                                                    "Thêm bài hát vào playlist thành công",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                playlist.songs.add(song)
                                            } else {
                                                Toast.makeText(
                                                    this@ViewAlbumActivity,
                                                    "Failed to add song to playlist",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }

                                        override fun onFailure(message: String) {
                                            Toast.makeText(
                                                this@ViewAlbumActivity,
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
                        viewAlbumViewModel.hideSong(
                            context = this,
                            song = it,
                            onHideSuccess = {
                                songAdapter?.notifyDataSetChanged()

                                if (viewAlbumViewModel.tempSongs.isEmpty()) {
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
                            viewAlbumViewModel.tempSongs.indexOf(it)
                        )
                        putSerializable(
                            Constant.BUNDLE_SONGS,
                            viewAlbumViewModel.tempSongs as Serializable
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
        binding.rvSong.layoutManager = LinearLayoutManager(this)
    }
}