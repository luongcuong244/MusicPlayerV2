package com.kma.musicplayerv2.ui.screen.listenrecently

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.kma.musicplayerv2.databinding.ActivityListenRecentlyBinding
import com.kma.musicplayerv2.ui.core.BaseActivity
import com.kma.musicplayerv2.R

class ListenRecentlyActivity : BaseActivity<ActivityListenRecentlyBinding>() {

    private lateinit var navController: NavController

    override fun getContentView(): Int = R.layout.activity_listen_recently

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController(R.id.nav_host_fragment)
        setupListeners()
    }

    private fun setupListeners() {
        binding.llSong.setOnClickListener {
            if (navController.currentDestination?.id != R.id.listenSongRecentlyFragment) {
                selectTab(R.id.listenSongRecentlyFragment)
                navController.navigate(R.id.listenSongRecentlyFragment)
            }
        }
        binding.llPlaylist.setOnClickListener {
            if (navController.currentDestination?.id != R.id.listenPlaylistRecentlyFragment) {
                selectTab(R.id.listenPlaylistRecentlyFragment)
                navController.navigate(R.id.listenPlaylistRecentlyFragment)
            }
        }
    }

    private fun selectTab(tabId: Int) {
        unSelectAllTabs()
        when (tabId) {
            R.id.listenSongRecentlyFragment -> {
                binding.tvSong.setTextColor(resources.getColor(R.color.color_925CFF))
                binding.vSong.setBackgroundColor(resources.getColor(R.color.color_925CFF))
            }
            R.id.listenPlaylistRecentlyFragment -> {
                binding.tvPlaylist.setTextColor(resources.getColor(R.color.color_925CFF))
                binding.vPlaylist.setBackgroundColor(resources.getColor(R.color.color_925CFF))
            }
        }
    }

    private fun unSelectAllTabs() {
        binding.tvSong.setTextColor(resources.getColor(R.color.color_969797))
        binding.tvPlaylist.setTextColor(resources.getColor(R.color.color_969797))
        binding.vSong.setBackgroundColor(resources.getColor(R.color.transparent))
        binding.vPlaylist.setBackgroundColor(resources.getColor(R.color.transparent))
    }
}