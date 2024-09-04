package com.kma.musicplayerv2.ui.screen.favouritesong

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.databinding.ActivityFavouriteSongBinding
import com.kma.musicplayerv2.ui.adapter.SongAdapter
import com.kma.musicplayerv2.ui.bottomsheet.FilterByArtistBottomSheet
import com.kma.musicplayerv2.ui.bottomsheet.SortByBottomSheet
import com.kma.musicplayerv2.ui.core.BaseActivity
import com.kma.musicplayerv2.ui.customview.VerticalSpaceItemDecoration

class FavouriteSongActivity : BaseActivity<ActivityFavouriteSongBinding>() {

    private lateinit var favouriteSongViewModel: FavouriteSongViewModel

    private lateinit var songAdapter: SongAdapter

    override fun getContentView(): Int = R.layout.activity_favourite_song

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favouriteSongViewModel = ViewModelProvider(this)[FavouriteSongViewModel::class.java]
        setupListeners()
        setupObservers()
        setupSongAdapter()
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
    }

    private fun setupObservers() {
        favouriteSongViewModel.sortBy.observe(this) {
            binding.tvSort.text = getString(it.textId)
            favouriteSongViewModel.sortSongs()
            songAdapter.notifyDataSetChanged()
        }
    }

    private fun setupSongAdapter() {
        songAdapter = SongAdapter(
            songs = favouriteSongViewModel.tempSongs,
            onClickMore = { },
            onClickUnFavourite = { song, position ->
                favouriteSongViewModel.unFavouriteSong(song)
                songAdapter.notifyItemRemoved(position)
            },
            onClickItem = { }
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