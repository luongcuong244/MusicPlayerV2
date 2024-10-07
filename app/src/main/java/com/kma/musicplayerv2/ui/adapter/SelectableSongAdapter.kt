package com.kma.musicplayerv2.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.databinding.LayoutItemSelectableSongBinding
import com.kma.musicplayerv2.model.SelectableSong

class SelectableSongAdapter(
    private val songs: MutableList<SelectableSong>,
    private val onSongClick: (Int) -> Unit
) : RecyclerView.Adapter<SelectableSongAdapter.SongViewHolder>() {

    private val tempSongs = mutableListOf<SelectableSong>()

    fun doFilter(text: String) {
        // If tempVideoPaths is empty, it means that the adapter has not been filtered yet
        if (tempSongs.isEmpty() && songs.isNotEmpty()) {
            tempSongs.addAll(songs)
        }

        val data = mutableListOf<SelectableSong>()

        if (text.isEmpty()) {
            data.addAll(tempSongs)
        } else {
            tempSongs.forEach {
                if (it.song.title.contains(text, ignoreCase = true)) {
                    data.add(it)
                }
            }
        }
        songs.clear()
        songs.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = LayoutItemSelectableSongBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = songs.size

    inner class SongViewHolder(val binding: LayoutItemSelectableSongBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val song = songs[position].song
            binding.tvTitle.text = song.title
            binding.tvArtistAndDuration.text = song.artist?.name ?: "Unknown Artist"

            Glide.with(binding.root)
                .load(song.thumbnail)
                .into(binding.ivThumbnail)

            binding.ivCheckbox.setImageResource(
                if (songs[position].isSelected) {
                    R.drawable.ic_select_all_enable
                } else {
                    R.drawable.ic_checkbox_unselected
                }
            )

            binding.root.setOnClickListener {
                onSongClick(position)
            }
        }
    }
}