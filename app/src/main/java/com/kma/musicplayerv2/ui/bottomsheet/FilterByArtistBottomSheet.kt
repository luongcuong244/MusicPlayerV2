package com.kma.musicplayerv2.ui.bottomsheet

import android.annotation.SuppressLint
import android.app.Dialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kma.musicplayerv2.databinding.BottomSheetFilterByArtistBinding
import com.kma.musicplayerv2.model.Artist
import com.kma.musicplayerv2.model.Song
import com.kma.musicplayerv2.ui.adapter.ArtistSelectionAdapter

class FilterByArtistBottomSheet(
    private val songs: List<Song>,
    private val filterByArtists: List<Artist>,
    private val onClickApply: (List<Artist>) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetFilterByArtistBinding
    private val items = mutableListOf<ArtistSelectionAdapter.ItemArtistSelection>()

    private fun groupByArtist(): Map<Artist, Int> {
        val map = mutableMapOf<Artist, Int>()
        songs.forEach { song ->
            if (!map.keys.any { it.id == song.artist.id }) {
                map[song.artist] = 1
            } else {
                val artist = map.keys.first { it.id == song.artist.id }
                map[artist] = map[artist]!! + 1
            }
        }
        return map
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        binding = BottomSheetFilterByArtistBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        initView()
        setupListeners()
    }

    private fun initView() {
        val artistMap = groupByArtist()
        artistMap.forEach { (artist, totalSong) ->
            var isSelected = false
            if (filterByArtists.isNotEmpty()) {
                isSelected = filterByArtists.any { it.id == artist.id }
            }
            items.add(ArtistSelectionAdapter.ItemArtistSelection(artist, totalSong, isSelected))
        }
        val adapter = ArtistSelectionAdapter(items)
        binding.rvArtist.adapter = adapter
        binding.rvArtist.layoutManager = LinearLayoutManager(context)
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            dismiss()
        }
        binding.rlApply.setOnClickListener {
            val selectedArtists = items.filter { it.isSelected }.map { it.artist }
            onClickApply(selectedArtists)
            dismiss()
        }
    }
}