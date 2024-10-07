package com.kma.musicplayerv2.ui.adapter

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.kma.musicplayerv2.databinding.ItemRankSongBinding
import com.kma.musicplayerv2.databinding.ItemSongBinding
import com.kma.musicplayerv2.model.Song

class RankAdapter(
    private val songs: List<Song>,
    private val onClickMore: (Song) -> Unit,
    private val onClickItem: (Song) -> Unit
) : RecyclerView.Adapter<RankAdapter.SongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = ItemRankSongBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(songs[position])
    }

    override fun getItemCount(): Int = songs.size

    inner class SongViewHolder(val binding: ItemRankSongBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song) {
            binding.tvTitle.text = song.title
            binding.tvArtist.text = song.artist.name
            binding.ivDownloaded.visibility = if (song.isDownloaded) {
                View.VISIBLE
            } else {
                View.GONE
            }
            binding.rlPremium.visibility = if (song.isPremium) {
                View.VISIBLE
            } else {
                View.GONE
            }
            binding.ivFavourite.visibility = View.GONE

            binding.ivMore.setOnClickListener {
                onClickMore(song)
            }

            binding.root.setOnClickListener {
                onClickItem(song)
            }

            binding.tvRank.text = (adapterPosition + 1).toString()

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
        }
    }
}