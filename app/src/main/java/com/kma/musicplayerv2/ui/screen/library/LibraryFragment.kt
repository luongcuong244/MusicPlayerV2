package com.kma.musicplayerv2.ui.screen.library

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kma.musicplayerv2.ui.core.BaseFragment
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.databinding.FragmentLibraryBinding
import com.kma.musicplayerv2.model.ListenRecently
import com.kma.musicplayerv2.ui.adapter.ListenRecentlyAdapter
import com.kma.musicplayerv2.ui.adapter.PlaylistAdapter
import com.kma.musicplayerv2.ui.customview.HorizontalSpaceItemDecoration
import com.kma.musicplayerv2.ui.customview.VerticalSpaceItemDecoration
import com.kma.musicplayerv2.ui.screen.favouritesong.FavouriteSongActivity
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
        listenRecentlyAdapter = ListenRecentlyAdapter(
            listOf(
                ListenRecently(
                    id = 1,
                    type = ListenRecently.Type.PLAYLIST,
                    name = "Playlist 1",
                    image = "https://images.pexels.com/photos/4162581/pexels-photo-4162581.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
                ),
                ListenRecently(
                    id = 1,
                    type = ListenRecently.Type.PLAYLIST,
                    name = "#Playlist 2",
                    image = "https://images.pexels.com/photos/5699509/pexels-photo-5699509.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
                ),
                ListenRecently(
                    id = 1,
                    type = ListenRecently.Type.PLAYLIST,
                    name = "Hay nghe",
                    image = "https://images.pexels.com/photos/5965930/pexels-photo-5965930.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
                ),
            ),
            { },
            { }
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

    private fun setupPlaylistAdapter() {
        libraryViewModel.fetchPlaylists(requireActivity(), onSuccess = {
            playlistAdapter = PlaylistAdapter(
                libraryViewModel.playlists,
                { },
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