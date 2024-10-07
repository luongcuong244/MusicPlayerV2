package com.kma.musicplayerv2.ui.customview

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.databinding.BottomMiniAudioPlayerBinding
import com.kma.musicplayerv2.service.PlaySongService
import com.kma.musicplayerv2.ui.core.BaseActivity
import com.kma.musicplayerv2.ui.screen.playsong.PlaySongActivity
import com.kma.musicplayerv2.utils.Constant

class BottomMiniAudioPlayer : FrameLayout {

    private var binding: BottomMiniAudioPlayerBinding =
        BottomMiniAudioPlayerBinding.inflate(LayoutInflater.from(context), this, true)

    lateinit var activity: BaseActivity<*>

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun initView(songService: PlaySongService) {
        songService.playingSong.observeForever {
            binding.tvName.text = songService.playingSong.value?.title
            binding.tvArtist.text = songService.playingSong.value?.artist?.name ?: "Unknow"
            Glide.with(context.applicationContext)
                .load((songService.playingSong.value)?.thumbnail)
                .placeholder(R.drawable.default_song_thumbnail)
                .into(binding.ivThumbnail)
        }
        songService.isPlaying.observeForever {
            if (it) {
                binding.ivPlayPause.setImageResource(R.drawable.ic_mini_pause)
            } else {
                binding.ivPlayPause.setImageResource(R.drawable.ic_mini_play)
            }
        }
        binding.ivPlayPause.setOnClickListener {
            if (songService.isPlaying.value == true) {
                songService.pause()
            } else {
                songService.resume()
            }
        }
        binding.ivNext.setOnClickListener {
            songService.playNext()
        }
        binding.root.setOnClickListener {
            activity.showActivityForResult(PlaySongActivity::class.java, 100, Bundle().apply {
                putBoolean(Constant.BUNDLE_IS_FROM_BOTTOM_MINI_PLAYER, true)
            })
        }
    }
}