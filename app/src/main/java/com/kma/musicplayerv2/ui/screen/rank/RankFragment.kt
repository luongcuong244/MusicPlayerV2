package com.kma.musicplayerv2.ui.screen.rank

import android.os.Bundle
import com.kma.musicplayer.ui.screen.core.BaseFragment
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.databinding.FragmentLibraryBinding

class RankFragment : BaseFragment<FragmentLibraryBinding>() {
    override fun getContentView(): Int = R.layout.fragment_personal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}