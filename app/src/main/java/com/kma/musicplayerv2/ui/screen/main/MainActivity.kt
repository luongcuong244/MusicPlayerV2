package com.kma.musicplayerv2.ui.screen.main

import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.databinding.ActivityMainBinding
import com.kma.musicplayerv2.ui.core.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private lateinit var navController: NavController

    override fun getContentView(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController(R.id.nav_host_fragment)
        setupListeners()
    }

    private fun setupListeners() {
        binding.llTabHome.setOnClickListener {
            if (navController.currentDestination?.id != R.id.libraryFragment) {
                selectTab(R.id.libraryFragment)
                navController.navigate(R.id.libraryFragment)
            }
        }
        binding.llTabFavourite.setOnClickListener {
            if (navController.currentDestination?.id != R.id.exploreFragment) {
                selectTab(R.id.exploreFragment)
                navController.navigate(R.id.exploreFragment)
            }
        }
        binding.llTabLocal.setOnClickListener {
            if (navController.currentDestination?.id != R.id.rankFragment) {
                selectTab(R.id.rankFragment)
                navController.navigate(R.id.rankFragment)
            }
        }
        binding.llTabSetting.setOnClickListener {
            if (navController.currentDestination?.id != R.id.personalFragment) {
                selectTab(R.id.personalFragment)
                navController.navigate(R.id.personalFragment)
            }
        }
    }

    private fun selectTab(tabId: Int) {
        unSelectAllTabs()
        when (tabId) {
            R.id.libraryFragment -> {
                binding.ivLibrary.setImageResource(R.drawable.ic_tab_library_active)
                binding.tvLibrary.setTextColor(resources.getColor(R.color.color_925CFF))
            }
            R.id.exploreFragment -> {
                binding.ivExplore.setImageResource(R.drawable.ic_tab_explore_active)
                binding.tvExplore.setTextColor(resources.getColor(R.color.color_925CFF))
            }
            R.id.rankFragment -> {
                binding.ivRank.setImageResource(R.drawable.ic_tab_rank_active)
                binding.tvRank.setTextColor(resources.getColor(R.color.color_925CFF))
            }
            R.id.personalFragment -> {
                binding.ivPersonal.setImageResource(R.drawable.ic_tab_personal_active)
                binding.tvPersonal.setTextColor(resources.getColor(R.color.color_925CFF))
            }
        }
    }

    private fun unSelectAllTabs() {
        binding.ivLibrary.setImageResource(R.drawable.ic_tab_library_inactive)
        binding.tvLibrary.setTextColor(resources.getColor(R.color.color_9C9D9D))
        binding.ivExplore.setImageResource(R.drawable.ic_tab_explore_inactive)
        binding.tvExplore.setTextColor(resources.getColor(R.color.color_9C9D9D))
        binding.ivRank.setImageResource(R.drawable.ic_tab_rank_inactive)
        binding.tvRank.setTextColor(resources.getColor(R.color.color_9C9D9D))
        binding.ivPersonal.setImageResource(R.drawable.ic_tab_personal_inactive)
        binding.tvPersonal.setTextColor(resources.getColor(R.color.color_9C9D9D))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        val currentFragment = navHostFragment?.childFragmentManager?.fragments?.get(0)
        currentFragment?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        val currentFragment = navHostFragment?.childFragmentManager?.fragments?.get(0)
        currentFragment?.onActivityResult(requestCode, resultCode, data)
    }
}