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
import com.kma.musicplayerv2.databinding.ItemSongCommentBinding
import com.kma.musicplayerv2.model.SongComment

class SongCommentAdapter(
    private val comments: List<SongComment>
) : RecyclerView.Adapter<SongCommentAdapter.SongCommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongCommentViewHolder {
        val binding =
            ItemSongCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongCommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongCommentViewHolder, position: Int) {
        holder.bind(comments[position])
    }

    override fun getItemCount(): Int = comments.size

    inner class SongCommentViewHolder(
        private val binding: ItemSongCommentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(songComment: SongComment) {
            binding.tvUserName.text = songComment.userName
            binding.tvContent.text = songComment.content
            binding.tvTime.text = songComment.createdAt
            binding.progressBar.visibility = View.VISIBLE
            Glide.with(binding.root.context)
                .load(songComment.userAvatar)
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
                .into(binding.ivAvatar)
        }
    }
}