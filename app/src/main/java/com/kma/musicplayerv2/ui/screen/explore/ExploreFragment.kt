package com.kma.musicplayerv2.ui.screen.explore

import android.os.Bundle
import com.kma.musicplayer.ui.screen.core.BaseFragment
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.databinding.FragmentLibraryBinding

class ExploreFragment : BaseFragment<FragmentLibraryBinding>() {
    override fun getContentView(): Int = R.layout.fragment_explore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}