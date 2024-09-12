package com.kma.musicplayerv2.ui.screen.hiddensong

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.ui.adapter.SelectableSongAdapter
import com.kma.musicplayerv2.databinding.ActivityHiddenSongBinding
import com.kma.musicplayerv2.model.Song
import com.kma.musicplayerv2.network.common.ApiCallback
import com.kma.musicplayerv2.network.retrofit.repository.SongRepository
import com.kma.musicplayerv2.ui.core.BaseActivity

class HiddenSongActivity : BaseActivity<ActivityHiddenSongBinding>() {

    private lateinit var hiddenSongViewModel: HiddenSongViewModel
    private var selectableSongAdapter: SelectableSongAdapter? = null

    override fun getContentView(): Int = R.layout.activity_hidden_song

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        setupListeners()
        setupObserver()
    }

    private fun initViewModel() {
        hiddenSongViewModel = ViewModelProvider(this)[HiddenSongViewModel::class.java]
        SongRepository.getHiddenSongs(
            object : ApiCallback<List<Song>> {
                override fun onSuccess(data: List<Song>?) {
                    hiddenSongViewModel.setSongs(data ?: emptyList())
                    setupRecyclerView()
                }

                override fun onFailure(message: String) {
                    Toast.makeText(this@HiddenSongActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun setupRecyclerView() {
        selectableSongAdapter = SelectableSongAdapter(hiddenSongViewModel.songs) {
            hiddenSongViewModel.songs[it].isSelected = !hiddenSongViewModel.songs[it].isSelected
            selectableSongAdapter?.notifyItemChanged(it)
            hiddenSongViewModel.checkSelectAll()
            hiddenSongViewModel.checkAtLeastOneSelected()
        }
        binding.rvSongs.adapter = selectableSongAdapter
    }

    private fun setupListeners() {
        binding.ivSelectAll.setOnClickListener {
            if (hiddenSongViewModel.isSelectAll.value == true) {
                hiddenSongViewModel.deselectAll()
            } else {
                hiddenSongViewModel.selectAll()
            }
            selectableSongAdapter?.notifyDataSetChanged()
        }
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                selectableSongAdapter?.doFilter(s.toString())
                hiddenSongViewModel.checkListEmpty()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        binding.llUnhide.setOnClickListener {
            val selectedSongs = hiddenSongViewModel.getSelectedSongs()
            SongRepository.unhideSongs(
                selectedSongs,
                object : ApiCallback<Boolean> {
                    override fun onSuccess(data: Boolean?) {
                        Toast.makeText(
                            this@HiddenSongActivity,
                            getString(R.string.unhide_songs_successfully),
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }

                    override fun onFailure(message: String) {
                        Toast.makeText(this@HiddenSongActivity, message, Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }

    private fun setupObserver() {
        hiddenSongViewModel.isSelectAll.observe(this) {
            binding.ivSelectAll.setImageResource(if (it) R.drawable.ic_select_all_enable else R.drawable.ic_select_all_disable)
        }
        hiddenSongViewModel.isAtLeastOneSelected.observe(this) {
            val opacity = if (it) 1.0f else 0.5f
            binding.llUnhide.alpha = opacity
            binding.llUnhide.isClickable = it
        }
        hiddenSongViewModel.isListEmpty.observe(this) {
            binding.tvNoSongs.visibility =
                if (it) android.view.View.VISIBLE else android.view.View.GONE
            binding.rvSongs.visibility =
                if (it) android.view.View.GONE else android.view.View.VISIBLE
        }
    }
}