package com.kma.musicplayerv2.ui.screen.explore

import android.os.Bundle
import android.view.View
import com.kma.musicplayerv2.ui.core.BaseFragment
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.databinding.FragmentExploreBinding
import com.kma.musicplayerv2.databinding.FragmentLibraryBinding

class ExploreFragment : BaseFragment<FragmentExploreBinding>() {
    override fun getContentView(): Int = R.layout.fragment_explore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}