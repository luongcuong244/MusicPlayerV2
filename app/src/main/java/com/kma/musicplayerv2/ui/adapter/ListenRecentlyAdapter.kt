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
import com.kma.musicplayerv2.databinding.ItemListenRecentlyBinding
import com.kma.musicplayerv2.model.Playlist

class ListenRecentlyAdapter(
    private val list: List<Playlist>,
    private val onClickItem: (Playlist) -> Unit,
    private val onClickViewAll: () -> Unit
) : RecyclerView.Adapter<ListenRecentlyAdapter.ListenRecentlyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListenRecentlyViewHolder {
        val binding = ItemListenRecentlyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ListenRecentlyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size + 2
    }

    override fun onBindViewHolder(holder: ListenRecentlyViewHolder, position: Int) {
        if (position == 0 || position == list.size + 1) {
            holder.bindViewAll(position == 0)
            return
        }
        holder.bindItem(list[position - 1])
    }

    inner class ListenRecentlyViewHolder(val binding: ItemListenRecentlyBinding) :
        ViewHolder(binding.root) {
        fun bindItem(listenRecently: Playlist) {
            binding.llViewAll.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
            Glide.with(binding.root.context)
                .load(listenRecently.image)
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
            binding.tvTitle.text = listenRecently.name

            binding.llItem.setOnClickListener {
                onClickItem(listenRecently)
            }
        }

        fun bindViewAll(isFirst: Boolean) {
            if (isFirst) {
                binding.llItem.visibility = View.VISIBLE
                binding.llViewAll.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                binding.ivThumb.setImageResource(R.drawable.img_default_listen_recently)
                binding.tvTitle.text = binding.root.context.getString(R.string.song_listen_recently)
                binding.llItem.setOnClickListener {
                    onClickViewAll()
                }
            } else {
                binding.llItem.visibility = View.GONE
                binding.llViewAll.visibility = View.VISIBLE
                binding.llViewAll.setOnClickListener {
                    onClickViewAll()
                }
            }
        }
    }
}