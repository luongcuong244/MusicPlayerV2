package com.kma.musicplayerv2.ui.screen.playsong

import android.content.ComponentName
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ui.DefaultTimeBar
import com.google.android.exoplayer2.ui.PlayerView
import com.kma.musicplayerv2.databinding.ActivityPlaySongBinding
import com.kma.musicplayerv2.model.RepeatMode
import com.kma.musicplayerv2.model.Song
import com.kma.musicplayerv2.model.nextMode
import com.kma.musicplayerv2.network.common.ApiCallback
import com.kma.musicplayerv2.network.retrofit.repository.SongRepository
import com.kma.musicplayerv2.ui.bottomsheet.SleepTimerBottomSheet
import com.kma.musicplayerv2.ui.bottomsheet.song_queue.SongQueueBottomSheet
import com.kma.musicplayerv2.ui.core.BaseActivity
import com.kma.musicplayerv2.utils.Constant
import com.kma.musicplayerv2.utils.SharePrefUtils
import com.kma.musicplayerv2.utils.ShareUtils


class PlaySongActivity : BaseActivity<ActivityPlaySongBinding>() {

    private var isFromMiniPlayer = false

    private var ivPrevious: ImageView? = null
    private var ivNext: ImageView? = null
    private var ivRandom: ImageView? = null
    private var ivRepeat: ImageView? = null
    private lateinit var ivFavourite: ImageView
    private lateinit var ivShare: ImageView
    private lateinit var llTimer: LinearLayout
    private lateinit var llQueue: LinearLayout
    private lateinit var tvSongName: TextView
    private lateinit var tvArtist: TextView
    private lateinit var ivThumbnail: ImageView
    private lateinit var ivTimer: ImageView
    private lateinit var playerView: PlayerView
    private var exo_duration: TextView? = null
    private var exo_progress: DefaultTimeBar? = null
    private var exo_controller: LinearLayout? = null

    override fun getContentView(): Int = com.kma.musicplayerv2.R.layout.activity_play_song

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isFromMiniPlayer = intent.getBooleanExtra(Constant.BUNDLE_IS_FROM_BOTTOM_MINI_PLAYER, false)

        ivPrevious = findViewById(com.kma.musicplayerv2.R.id.iv_previous)
        ivNext = findViewById(com.kma.musicplayerv2.R.id.iv_next)
        ivRandom = findViewById(com.kma.musicplayerv2.R.id.iv_random)
        ivRepeat = findViewById(com.kma.musicplayerv2.R.id.iv_repeat)
        exo_duration = findViewById(com.kma.musicplayerv2.R.id.exo_duration)
        exo_progress = findViewById(com.kma.musicplayerv2.R.id.exo_progress)
        exo_controller = findViewById(com.kma.musicplayerv2.R.id.audio_controller_root)
        ivFavourite = findViewById(com.kma.musicplayerv2.R.id.iv_favourite)
        ivShare = findViewById(com.kma.musicplayerv2.R.id.iv_share)
        llTimer = findViewById(com.kma.musicplayerv2.R.id.ll_timer)
        llQueue = findViewById(com.kma.musicplayerv2.R.id.ll_queue)
        tvSongName = findViewById(com.kma.musicplayerv2.R.id.tv_song_name)
        tvArtist = findViewById(com.kma.musicplayerv2.R.id.tv_artist)
        ivThumbnail = findViewById(com.kma.musicplayerv2.R.id.iv_thumbnail)
        ivTimer = findViewById(com.kma.musicplayerv2.R.id.iv_timer)
        playerView = findViewById(com.kma.musicplayerv2.R.id.player_view)

        if (!isFromMiniPlayer && SharePrefUtils.getSongIds().isNullOrEmpty()) {
            val songs = intent.getSerializableExtra(Constant.BUNDLE_SONGS)
            val currentIndex = intent.getIntExtra(Constant.BUNDLE_START_FROM_INDEX, 0)
            SharePrefUtils.saveSongIds((songs as MutableList<Song>).map { it.id })
            SharePrefUtils.saveCurrentSongIndex(currentIndex)
        }
    }

    override fun onServiceConnected(className: ComponentName, service: IBinder) {
        super.onServiceConnected(className, service)
        setupPlayers()
        setupListeners()
        setupObservers()
    }

    private fun setupPlayers() {
        if (!isFromMiniPlayer) {
            val songs = intent.getSerializableExtra(Constant.BUNDLE_SONGS)
            songService?.songs?.clear()
            songService?.addMore(songs as MutableList<Song>)
        }
        playerView.player = songService?.audioPlayerManager?.simpleExoPlayer
        setupObservers()
        setupListeners()
        if (!isFromMiniPlayer) {
            val currentIndex = intent.getIntExtra(Constant.BUNDLE_START_FROM_INDEX, 0)
            songService?.playAt(currentIndex)
        } else {
            playerView.showController()
        }
    }

    private fun setupListeners() {
        ivFavourite.setOnClickListener {
            val song = songService?.playingSong?.value ?: return@setOnClickListener
            if (song.isFavourite) {
                SongRepository.removeFavouriteSong(
                    song,
                    object : ApiCallback<Void> {
                        override fun onSuccess(data: Void?) {
                            song.isFavourite = false
                            ivFavourite.setImageResource(com.kma.musicplayerv2.R.drawable.ic_not_favourite)
                        }

                        override fun onFailure(message: String) {
                            Toast.makeText(this@PlaySongActivity, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            } else {
                SongRepository.addFavouriteSong(
                    song,
                    object : ApiCallback<Void> {
                        override fun onSuccess(data: Void?) {
                            song.isFavourite = true
                            ivFavourite.setImageResource(com.kma.musicplayerv2.R.drawable.ic_favourite)
                        }

                        override fun onFailure(message: String) {
                            Toast.makeText(this@PlaySongActivity, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        }
        ivShare.setOnClickListener {
            val song = songService?.playingSong?.value ?: return@setOnClickListener
            ShareUtils.shareSong(this, song)
        }
        llTimer.setOnClickListener {
            songService?.let {
                val bottomSheet = SleepTimerBottomSheet(supportFragmentManager, it)
                bottomSheet.show(supportFragmentManager, bottomSheet.tag)
            }
        }
        ivRandom?.setOnClickListener {
            songService?.setPlayRandomlyEnabled(
                !(songService?.isPlayRandomlyEnabled?.value ?: false)
            )
        }
        ivRepeat?.setOnClickListener {
            songService?.setRepeatMode(
                songService?.repeatMode?.value?.nextMode() ?: RepeatMode.NONE
            )
        }
        llQueue.setOnClickListener {
            songService?.let {
                val bottomSheet = SongQueueBottomSheet(
                    songs = it.songs,
                    playingSongIndex = it.currentIndex,
                    onPlayingSongIndexChanged = { newIndex ->
                        it.changeCurrentIndex(newIndex)
                    }
                )
                bottomSheet.show(supportFragmentManager, bottomSheet.tag)
            }
        }
        findViewById<ImageView>(com.kma.musicplayerv2.R.id.exo_play).setOnClickListener {
            songService?.resume()
            findViewById<ImageView>(com.kma.musicplayerv2.R.id.exo_play).visibility = View.GONE
            findViewById<ImageView>(com.kma.musicplayerv2.R.id.exo_pause).visibility = View.VISIBLE
        }
        findViewById<ImageView>(com.kma.musicplayerv2.R.id.exo_pause).setOnClickListener {
            songService?.pause()
            findViewById<ImageView>(com.kma.musicplayerv2.R.id.exo_play).visibility = View.VISIBLE
            findViewById<ImageView>(com.kma.musicplayerv2.R.id.exo_pause).visibility = View.GONE
        }
        ivPrevious?.setOnClickListener {
            songService?.playPrevious()
        }
        ivNext?.setOnClickListener {
            songService?.playNext()
        }
    }

    private fun setupObservers() {
        songService?.sleepTimerModel?.observe(this) {
            if (it != null) {
                ivTimer.setImageResource(com.kma.musicplayerv2.R.drawable.ic_timer_on)
            } else {
                ivTimer.setImageResource(com.kma.musicplayerv2.R.drawable.ic_timer_off)
            }
        }
        songService?.isPlayRandomlyEnabled?.observe(this) {
            ivRandom?.setImageResource(if (it) com.kma.musicplayerv2.R.drawable.ic_random_on else com.kma.musicplayerv2.R.drawable.ic_random_off)
        }
        songService?.repeatMode?.observe(this) {
            when (it) {
                RepeatMode.NONE -> {
                    ivRepeat?.setImageResource(com.kma.musicplayerv2.R.drawable.ic_no_repeat)
                }

                RepeatMode.REPEAT_ALL -> {
                    ivRepeat?.setImageResource(com.kma.musicplayerv2.R.drawable.ic_repeat_forever)
                }

                RepeatMode.REPEAT_ONE -> {
                    ivRepeat?.setImageResource(com.kma.musicplayerv2.R.drawable.ic_repeat_one)
                }
            }
        }
        songService?.playingSong?.observe(this) {
            updateUIBasedOnCurrentSong()
        }
    }

    private fun updateUIBasedOnCurrentSong() {
        songService?.playingSong?.value?.let {
            tvSongName.text = it.title
            tvArtist.text = it.artist.name
            if (it.isFavourite) {
                ivFavourite.setImageResource(com.kma.musicplayerv2.R.drawable.ic_favourite)
            } else {
                ivFavourite.setImageResource(com.kma.musicplayerv2.R.drawable.ic_not_favourite)
            }
            Glide.with(this)
                .load(it.thumbnail)
                .placeholder(com.kma.musicplayerv2.R.drawable.default_song_thumbnail)
                .into(ivThumbnail)
        }
    }
}