package com.kma.musicplayerv2.ui.screen.rank

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.kma.musicplayerv2.ui.core.BaseFragment
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.databinding.FragmentRankBinding

class RankFragment : BaseFragment<FragmentRankBinding>() {
    private lateinit var navController: NavController

    override fun getContentView(): Int = R.layout.fragment_rank

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = requireActivity().findNavController(R.id.nav_host_fragment1)
        setupListeners()
    }

    private fun setupListeners() {
        binding.rlView.setOnClickListener {
            if (navController.currentDestination?.id != R.id.rankByViewFragment) {
                selectTab(R.id.rankByViewFragment)
                navController.navigate(R.id.rankByViewFragment)
            }
        }
        binding.rlFavourite.setOnClickListener {
            if (navController.currentDestination?.id != R.id.rankByFavouriteFragment) {
                selectTab(R.id.rankByFavouriteFragment)
                navController.navigate(R.id.rankByFavouriteFragment)
            }
        }
    }

    private fun selectTab(tabId: Int) {
        unSelectAllTabs()
        when (tabId) {
            R.id.rankByViewFragment -> {
                binding.rlView.setBackgroundResource(R.drawable.bg_purple_button)
            }
            R.id.rankByFavouriteFragment -> {
                binding.rlFavourite.setBackgroundResource(R.drawable.bg_purple_button)
            }
        }
    }

    private fun unSelectAllTabs() {
        binding.rlView.setBackgroundResource(R.drawable.bg_gray_button)
        binding.rlFavourite.setBackgroundResource(R.drawable.bg_gray_button)
    }
}