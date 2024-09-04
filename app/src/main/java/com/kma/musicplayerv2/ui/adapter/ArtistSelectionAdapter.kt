package com.kma.musicplayerv2.ui.adapter

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.databinding.ItemArtistSelectionBinding
import com.kma.musicplayerv2.model.Artist

class ArtistSelectionAdapter(
    private val items: List<ItemArtistSelection>,
) : RecyclerView.Adapter<ArtistSelectionAdapter.ArtistSelectionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistSelectionViewHolder {
        val binding = ItemArtistSelectionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ArtistSelectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArtistSelectionViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ArtistSelectionViewHolder(val binding: ItemArtistSelectionBinding) :
        ViewHolder(binding.root) {
        fun bind(item: ItemArtistSelection) {
            binding.tvTitle.text = item.artist.name
            binding.tvNumberOfSongs.text =
                "${item.totalSong} ${binding.root.context.getString(R.string.song)}"
            if (item.isSelected) {
                binding.ivCheck.setImageResource(R.drawable.ic_check)
            } else {
                binding.ivCheck.setImageResource(R.drawable.ic_uncheck)
            }
            binding.progressBar.visibility = View.VISIBLE
            Glide.with(binding.root.context)
                .load(item.artist.image)
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
            binding.root.setOnClickListener {
                item.isSelected = !item.isSelected
                if (item.isSelected) {
                    binding.ivCheck.setImageResource(R.drawable.ic_check)
                } else {
                    binding.ivCheck.setImageResource(R.drawable.ic_uncheck)
                }
                notifyItemChanged(adapterPosition)
            }
        }
    }

    data class ItemArtistSelection(
        val artist: Artist,
        val totalSong: Int,
        var isSelected: Boolean = false
    )
}