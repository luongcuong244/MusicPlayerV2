package com.kma.musicplayerv2.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.databinding.DialogCreatePlaylistBinding
import com.kma.musicplayerv2.model.Playlist
import com.kma.musicplayerv2.network.common.ApiCallback
import com.kma.musicplayerv2.network.retrofit.repository.PlaylistRepository

class CreatePlaylistDialog(
    private val onPlaylistCreated: (Playlist) -> Unit
) : DialogFragment() {
    private lateinit var binding: DialogCreatePlaylistBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BaseDialog)
        binding = DialogCreatePlaylistBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    fun setupListeners() {
        binding.rlCancel.setOnClickListener {
            dismiss()
        }
        binding.rlSave.setOnClickListener {
            val name = binding.etPlaylistName.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(
                    context,
                    context?.getString(R.string.please_enter_playlist_name),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            PlaylistRepository.createPlaylist(name, object : ApiCallback<Playlist> {
                override fun onSuccess(data: Playlist?) {
                    if (data == null) {
                        onFailure("Unknown error")
                        return
                    }
                    onPlaylistCreated(data)
                    dismiss()
                }

                override fun onFailure(message: String) {
                    Toast.makeText(
                        context,
                        "Failed to create playlist: $message",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }
}