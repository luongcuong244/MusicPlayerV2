package com.kma.musicplayerv2.ui.bottomsheet

import android.annotation.SuppressLint
import android.app.Dialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kma.musicplayerv2.databinding.BottomSheetSongOptionBinding

class SongOptionBottomSheet : BottomSheetDialogFragment() {

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

    }

    private fun setupListeners() {

    }
}