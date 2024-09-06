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
import com.kma.musicplayerv2.databinding.BottomSheetSongOptionBinding
import com.kma.musicplayerv2.model.Song

class SongOptionBottomSheet(
    private val song: Song,
    private val onClickShare: ((Song) -> Unit)? = null,
    private val onDeleteFromPlaylist: ((Song) -> Unit)? = null,
    private val onClickDownload: ((Song) -> Unit)? = null,
    private val onClickAddToFavorite: ((Song) -> Unit)? = null,
    private val onClickAddToPlaylist: ((Song) -> Unit)? = null,
    private val onClickPlayNext: ((Song) -> Unit)? = null,
    private val onClickHideSong: ((Song) -> Unit)? = null,
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetSongOptionBinding

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        binding = BottomSheetSongOptionBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        initView()
        setupListeners()
    }

    private fun initView() {
        if (onClickShare == null) {
            binding.ivShare.visibility = View.GONE
        }
        if (onDeleteFromPlaylist == null) {
            binding.llDeleteFromPlaylist.visibility = View.GONE
        }
        if (onClickDownload == null) {
            binding.llDownload.visibility = View.GONE
        }
        if (onClickAddToFavorite == null) {
            binding.llAddToFavourite.visibility = View.GONE
        }
        if (onClickAddToPlaylist == null) {
            binding.llAddToPlaylist.visibility = View.GONE
        }
        if (onClickPlayNext == null) {
            binding.llPlayNext.visibility = View.GONE
        }
        if (onClickHideSong == null) {
            binding.llHideSong.visibility = View.GONE
        }

        binding.tvTitle.text = song.title
        binding.tvArtist.text = song.artist.name
        binding.progressBar.visibility = View.VISIBLE
        Glide.with(binding.root.context)
            .load(song.thumbnail)
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
        if (song.isFavourite) {
            binding.ivFavourite.setImageResource(R.drawable.ic_favourite)
            binding.tvFavourite.text = getString(R.string.added_to_favourite_list)
        } else {
            binding.ivFavourite.setImageResource(R.drawable.ic_not_favourite)
            binding.tvFavourite.text = getString(R.string.add_to_favourite_list)
        }
    }

    private fun setupListeners() {
        binding.ivShare.setOnClickListener {
            onClickShare?.invoke(song)
            dismiss()
        }
        binding.llDeleteFromPlaylist.setOnClickListener {
            onDeleteFromPlaylist?.invoke(song)
            dismiss()
        }
        binding.llDownload.setOnClickListener {
            onClickDownload?.invoke(song)
            dismiss()
        }
        binding.llAddToFavourite.setOnClickListener {
            onClickAddToFavorite?.invoke(song)
            dismiss()
        }
        binding.llAddToPlaylist.setOnClickListener {
            onClickAddToPlaylist?.invoke(song)
            dismiss()
        }
        binding.llPlayNext.setOnClickListener {
            onClickPlayNext?.invoke(song)
            dismiss()
        }
        binding.llHideSong.setOnClickListener {
            onClickHideSong?.invoke(song)
            dismiss()
        }
    }
}