package com.kma.musicplayerv2.ui.customview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class HorizontalSpaceItemDecoration(private val horizontalSpaceHeight: Int) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if ((parent.getChildAdapterPosition(view) != (parent.adapter?.itemCount ?: 0) - 1)) {
            outRect.right = horizontalSpaceHeight
        }
    }
}