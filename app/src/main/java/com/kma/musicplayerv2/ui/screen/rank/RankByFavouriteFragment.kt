package com.kma.musicplayerv2.ui.screen.rank

import android.os.Bundle
import android.view.View
import com.kma.musicplayerv2.ui.core.BaseFragment
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.databinding.FragmentRankBinding
import com.kma.musicplayerv2.databinding.FragmentRankByFavouriteBinding

class RankByFavouriteFragment : BaseFragment<FragmentRankByFavouriteBinding>() {
    override fun getContentView(): Int = R.layout.fragment_rank_by_favourite

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}