package com.kma.musicplayerv2.ui.bottomsheet

import android.annotation.SuppressLint
import android.app.Dialog
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.intuit.sdp.R
import com.kma.musicplayerv2.databinding.BottomSheetAddToPlaylistBinding
import com.kma.musicplayerv2.model.Playlist
import com.kma.musicplayerv2.network.common.ApiCallback
import com.kma.musicplayerv2.network.retrofit.repository.PlaylistRepository
import com.kma.musicplayerv2.ui.adapter.PlaylistAdapter
import com.kma.musicplayerv2.ui.customview.VerticalSpaceItemDecoration
import com.kma.musicplayerv2.ui.dialog.CreatePlaylistDialog

class AddToPlaylistBottomSheet(
    private val onClickPlaylist: (Playlist) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetAddToPlaylistBinding
    private lateinit var playlistAdapter: PlaylistAdapter
    private var playlists = mutableListOf<Playlist>()

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        binding = BottomSheetAddToPlaylistBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        initView()
        setupListeners()
    }

    private fun initView() {
        PlaylistRepository.getAllPlaylists(
            object : ApiCallback<List<Playlist>> {
                override fun onSuccess(data: List<Playlist>?) {
                    if (data == null) {
                        onFailure("Unknown error")
                        return
                    }
                    playlists.clear()
                    playlists.addAll(data)
                    playlistAdapter = PlaylistAdapter(
                        playlists = playlists,
                        onClickCreateNewPlaylist = {
                            val createPlaylistDialog = CreatePlaylistDialog(
                                onPlaylistCreated = {
                                    playlists.add(0, it)
                                    playlistAdapter.notifyDataSetChanged()
                                }
                            )
                            createPlaylistDialog.show(
                                requireActivity().supportFragmentManager,
                                createPlaylistDialog.tag
                            )
                        },
                        onClickItem = {
                            onClickPlaylist(it)
                            dismiss()
                        }
                    )
                    binding.rvPlaylist.adapter = playlistAdapter
                    binding.rvPlaylist.addItemDecoration(
                        VerticalSpaceItemDecoration(
                            requireActivity().resources.getDimension(
                                R.dimen._13sdp
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

    private fun setupListeners() {

    }
}