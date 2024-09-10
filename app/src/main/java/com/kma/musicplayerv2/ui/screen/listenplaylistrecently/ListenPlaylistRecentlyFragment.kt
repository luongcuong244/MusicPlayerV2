package com.kma.musicplayerv2.ui.screen.listenplaylistrecently

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.kma.musicplayerv2.ui.core.BaseFragment
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.databinding.FragmentListenPlaylistRecentlyBinding
import com.kma.musicplayerv2.model.Playlist
import com.kma.musicplayerv2.network.common.ApiCallback
import com.kma.musicplayerv2.network.retrofit.repository.PlaylistRepository
import com.kma.musicplayerv2.ui.adapter.PlaylistAdapter
import com.kma.musicplayerv2.ui.customview.VerticalSpaceItemDecoration
import com.kma.musicplayerv2.ui.screen.viewplaylist.ViewPlaylistActivity
import com.kma.musicplayerv2.utils.Constant

class ListenPlaylistRecentlyFragment : BaseFragment<FragmentListenPlaylistRecentlyBinding>() {

    private lateinit var playlistAdapter: PlaylistAdapter

    override fun getContentView(): Int = R.layout.fragment_listen_playlist_recently

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        PlaylistRepository.getRecentlyPlaylist(
            object : ApiCallback<List<Playlist>> {
                override fun onSuccess(data: List<Playlist>?) {
                    if (data == null) {
                        onFailure("Unknown error")
                        return
                    }
                    playlistAdapter = PlaylistAdapter(
                        playlists = data,
                        onClickCreateNewPlaylist = {

                        },
                        onClickItem = {
                            showActivity(ViewPlaylistActivity::class.java, Bundle().apply {
                                putSerializable(Constant.BUNDLE_PLAYLIST, it)
                            })
                        }
                    )
                    binding.rvPlaylist.adapter = playlistAdapter
                    binding.rvPlaylist.addItemDecoration(
                        VerticalSpaceItemDecoration(
                            requireActivity().resources.getDimension(
                                com.intuit.sdp.R.dimen._13sdp
                            ).toInt()
                        )
                    )
                    binding.rvPlaylist.layoutManager = LinearLayoutManager(context)
                }

                override fun onFailure(message: String) {
                    Toast.makeText(
                        context,
                        "Failed to fetch playlists: $message",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }
}