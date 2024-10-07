package com.kma.musicplayerv2.ui.bottomsheet

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.databinding.BottomSheetPlaylistOptionBinding
import com.kma.musicplayerv2.model.Playlist

class PlaylistOptionBottomSheet(
    private val playlist: Playlist,
    private val onClickShare: ((Playlist) -> Unit)? = null,
    private val onClickDownload: ((Playlist) -> Unit)? = null,
    private val onDeletePlaylist: ((Playlist) -> Unit)? = null,
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetPlaylistOptionBinding

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        binding = BottomSheetPlaylistOptionBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        initView()
        setupListeners()
    }

    private fun initView() {
        if (onClickShare == null) {
            binding.llSharePlaylist.visibility = View.GONE
        }
        if (onClickDownload == null) {
            binding.llDownloadPlaylist.visibility = View.GONE
        }
        if (onDeletePlaylist == null) {
            binding.llDeletePlaylist.visibility = View.GONE
        }

        binding.tvTitle.text = playlist.name
        binding.tvNumberOfSongs.text = "${playlist.songs.size} ${binding.root.context.getString(R.string.song)}"
        binding.progressBar.visibility = View.VISIBLE
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
        binding.llSharePlaylist.setOnClickListener {
            onClickShare?.invoke(playlist)
            dismiss()
        }
        binding.llDownloadPlaylist.setOnClickListener {
            onClickDownload?.invoke(playlist)
            dismiss()
        }
        binding.llDeletePlaylist.setOnClickListener {
            onDeletePlaylist?.invoke(playlist)
            dismiss()
        }
    }
}