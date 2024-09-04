package com.kma.musicplayerv2.ui.screen.personal

import android.os.Bundle
import com.kma.musicplayerv2.ui.core.BaseFragment
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.databinding.FragmentLibraryBinding

class PersonalFragment : BaseFragment<FragmentLibraryBinding>() {
    override fun getContentView(): Int = R.layout.fragment_rank

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}