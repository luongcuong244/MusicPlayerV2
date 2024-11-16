package com.kma.musicplayerv2.ui.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kma.musicplayerv2.databinding.ItemCategoryBinding
import com.kma.musicplayerv2.model.Category
import com.kma.musicplayerv2.ui.core.BaseActivity
import com.kma.musicplayerv2.ui.customview.HorizontalSpaceItemDecoration
import com.kma.musicplayerv2.ui.screen.viewalbum.ViewAlbumActivity
import com.kma.musicplayerv2.utils.Constant

class CategoryAdapter(
    private val activity: BaseActivity<*>,
    private val categories: List<Category>,
): RecyclerView.Adapter<CategoryAdapter.AlbumViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    inner class AlbumViewHolder(val binding: ItemCategoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            binding.tvTitle.text = category.name

            val albumAdapter = AlbumAdapter(
                category.albums
            ) {
                // on click
                activity.showActivity(ViewAlbumActivity::class.java, Bundle().apply {
                    putSerializable(Constant.BUNDLE_ALBUM, it)
                })
            }
            binding.rvAlbum.adapter = albumAdapter
            binding.rvAlbum.layoutManager = LinearLayoutManager(
                binding.root.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            // add spacing
            if (binding.rvAlbum.itemDecorationCount == 0) {
                binding.rvAlbum.addItemDecoration(
                    HorizontalSpaceItemDecoration(
                        binding.root.context.resources.getDimension(
                            com.intuit.sdp.R.dimen._13sdp
                        ).toInt()
                    )
                )
            }
        }
    }
}