package com.kma.musicplayerv2.ui.screen.library

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kma.musicplayerv2.ui.core.BaseFragment
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.databinding.FragmentLibraryBinding
import com.kma.musicplayerv2.ui.adapter.ListenRecentlyAdapter
import com.kma.musicplayerv2.ui.adapter.PlaylistAdapter
import com.kma.musicplayerv2.ui.customview.HorizontalSpaceItemDecoration
import com.kma.musicplayerv2.ui.customview.VerticalSpaceItemDecoration
import com.kma.musicplayerv2.ui.screen.favouritesong.FavouriteSongActivity
import com.kma.musicplayerv2.ui.screen.listenrecently.ListenRecentlyActivity
import com.kma.musicplayerv2.ui.screen.viewplaylist.ViewPlaylistActivity
import com.kma.musicplayerv2.utils.Constant
import com.kma.musicplayerv2.utils.FileUtils

class LibraryFragment : BaseFragment<FragmentLibraryBinding>() {

    private lateinit var libraryViewModel: LibraryViewModel
    private lateinit var listenRecentlyAdapter: ListenRecentlyAdapter
    private lateinit var playlistAdapter: PlaylistAdapter

    override fun getContentView(): Int = R.layout.fragment_library

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        libraryViewModel = ViewModelProvider(this)[LibraryViewModel::class.java]
        initView()
        setupListeners()
        setupListenRecentlyAdapter()
        setupPlaylistAdapter()
    }

    private fun initView() {
        libraryViewModel.fetchTotalOfFavoriteSongs(requireActivity()) {
            binding.tvFavouriteCount.text = it.toString()
        }
        binding.tvDownloadedCount.text = FileUtils.getDownloadedSongs(requireActivity()).size.toString()
        libraryViewModel.fetchUploadSongs(requireActivity()) {
            binding.tvUploadCount.text = it.size.toString()
        }
    }

    private fun setupListeners() {
        binding.llFavourite.setOnClickListener {
            showActivity(FavouriteSongActivity::class.java)
        }
    }

    private fun setupListenRecentlyAdapter() {
        libraryViewModel.fetchRecentlyPlaylist(requireActivity()) {
            listenRecentlyAdapter = ListenRecentlyAdapter(
                libraryViewModel.recentlyPlaylists,
                {
                    showActivity(ViewPlaylistActivity::class.java, Bundle().apply {
                        putSerializable(Constant.BUNDLE_PLAYLIST, it)
                    })
                },
                {
                    showActivity(ListenRecentlyActivity::class.java)
                }
            )
            binding.rvListenRecently.adapter = listenRecentlyAdapter
            binding.rvListenRecently.addItemDecoration(
                HorizontalSpaceItemDecoration(
                    requireActivity().resources.getDimension(
                        com.intuit.sdp.R.dimen._13sdp
                    ).toInt()
                )
            )
            binding.rvListenRecently.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupPlaylistAdapter() {
        libraryViewModel.fetchPlaylists(requireActivity(), onSuccess = {
            playlistAdapter = PlaylistAdapter(
                libraryViewModel.playlists,
                {
                    showActivity(ViewPlaylistActivity::class.java, Bundle().apply {
                        putSerializable(Constant.BUNDLE_PLAYLIST, it)
                    })
                },
                {}
            )
            binding.rvYourPlaylist.adapter = playlistAdapter
            binding.rvYourPlaylist.addItemDecoration(
                VerticalSpaceItemDecoration(
                    requireActivity().resources.getDimension(
                        com.intuit.sdp.R.dimen._13sdp
                    ).toInt()
                )
            )
            binding.rvYourPlaylist.layoutManager = LinearLayoutManager(context)
        })
    }
}