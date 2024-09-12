package com.kma.musicplayerv2.ui.screen.addwidget

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kma.musicplayerv2.databinding.LayoutItemAddWidgetGuideBinding

class AddWidgetGuideAdapter : RecyclerView.Adapter<AddWidgetGuideAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutItemAddWidgetGuideBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(AddWidgetGuideModel.values()[position])
    }

    override fun getItemCount(): Int = AddWidgetGuideModel.values().size

    inner class ViewHolder(val binding: LayoutItemAddWidgetGuideBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: AddWidgetGuideModel) {
            binding.imgGuide.setImageResource(model.img)
            binding.tvTitle.setText(binding.root.context.getString(model.title))
            binding.tvDescription.setText(binding.root.context.getString(model.content))
        }
    }
}