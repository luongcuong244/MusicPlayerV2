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
import com.kma.musicplayerv2.databinding.ItemAlbumBinding
import com.kma.musicplayerv2.model.Album

class AlbumAdapter(
    private val albums: List<Album>,
    private val onClickItem: (Album) -> Unit,
) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
            val binding = ItemAlbumBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return AlbumViewHolder(binding)
        }

        override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
            holder.bind(albums[position])
        }

        override fun getItemCount(): Int {
            return albums.size
        }

        inner class AlbumViewHolder(val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(album: Album) {
                binding.tvTitle.text = album.description
                binding.progressBar.visibility = View.VISIBLE
                Glide.with(binding.root.context)
                    .load(album.thumbnail)
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
                    onClickItem(album)
                }
            }
        }
}