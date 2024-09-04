package com.kma.musicplayerv2.ui.bottomsheet

import android.annotation.SuppressLint
import android.app.Dialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kma.musicplayerv2.databinding.BottomSheetSortByBinding
import com.kma.musicplayerv2.model.SortType

class SortByBottomSheet(
    private val sortBy: SortType,
    private val onSortBy: (SortType) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetSortByBinding

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        binding = BottomSheetSortByBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        initView()
        setupListeners()
    }

    private fun initView() {
        unSelectAll()
        when (sortBy) {
            SortType.NEWEST -> {
                binding.ivTickNewest.visibility = android.view.View.VISIBLE
            }
            SortType.OLDEST -> {
                binding.ivTickOldest.visibility = android.view.View.VISIBLE
            }
            SortType.SONG_NAME -> {
                binding.ivTickSongNameAZ.visibility = android.view.View.VISIBLE
            }
            SortType.ARTIST_NAME -> {
                binding.ivTickArtistNameAZ.visibility = android.view.View.VISIBLE
            }
        }
    }

    private fun setupListeners () {
        binding.clSortNewest.setOnClickListener {
            onSortBy(SortType.NEWEST)
            dismiss()
        }

        binding.clSortOldest.setOnClickListener {
            onSortBy(SortType.OLDEST)
            dismiss()
        }

        binding.clSortSongNameAZ.setOnClickListener {
            onSortBy(SortType.SONG_NAME)
            dismiss()
        }

        binding.clSortArtistNameAZ.setOnClickListener {
            onSortBy(SortType.ARTIST_NAME)
            dismiss()
        }
    }

    private fun unSelectAll() {
        binding.ivTickNewest.visibility = android.view.View.GONE
        binding.ivTickOldest.visibility = android.view.View.GONE
        binding.ivTickSongNameAZ.visibility = android.view.View.GONE
        binding.ivTickArtistNameAZ.visibility = android.view.View.GONE
    }
}