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
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.databinding.ItemPlaylistBinding
import com.kma.musicplayerv2.model.Playlist

class PlaylistAdapter(
    private val playlists: List<Playlist>,
    private val onClickItem: (Playlist) -> Unit,
    private val onClickCreateNewPlaylist: () -> Unit
) : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding = ItemPlaylistBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlaylistViewHolder(binding)
    }

    override fun getItemCount(): Int = playlists.size + 1

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        if (position == 0) {
            holder.bindCreateNewPlaylist()
            return
        }
        holder.bindItem(playlists[position - 1])
    }

    inner class PlaylistViewHolder(val binding: ItemPlaylistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItem(playlist: Playlist) {
            binding.tvTitle.text = playlist.name
            binding.tvNumberOfSongs.text = "${playlist.songs.size} ${binding.root.context.getString(R.string.song)}"
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
            binding.llItem.setOnClickListener {
                onClickItem(playlist)
            }
        }

        fun bindCreateNewPlaylist() {
            binding.llItem.visibility = View.GONE
            binding.llCreatePlaylist.visibility = View.VISIBLE
            binding.llCreatePlaylist.setOnClickListener {
                onClickCreateNewPlaylist()
            }
        }
    }
}