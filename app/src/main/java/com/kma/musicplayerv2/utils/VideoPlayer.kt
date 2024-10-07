package com.kma.musicplayerv2.utils

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.kma.musicplayerv2.model.VideoPart
import com.kma.musicplayerv2.network.common.ServerAddress
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VideoPlayer(
    private val context: Context,
    private val videoView1: SurfaceView,
) {

    private val mediaPlayer1: MediaPlayer = MediaPlayer()
    private val mediaPlayer2: MediaPlayer = MediaPlayer()

    init {
        videoView1.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                mediaPlayer1.setDisplay(holder)
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                getMediaPlayerPlaying().setDisplay(null)
                getMediaPlayerPaused().setDisplay(null)
            }
        })
    }

    var isVideoView1Playing = true
    var currentVideoIndex = 0

    private val videoParts = mutableListOf<VideoPart>().apply {
        var from = 0L
        for (i in 0..19) {
            val fileName = if (i < 10) "0$i" else i
            add(
                VideoPart(
                    url = "${ServerAddress.SERVER_ADDRESS}/uploads/test/output0$fileName.mp4",
                    from = from,
                    to = from + 3000
                )
            )
            from += 3000
        }
    }

    private val videoPartNames = mutableListOf<String>().apply {
        for (i in 0..19) {
            val fileName = if (i < 10) "0$i" else i
            add("output0$fileName.mp4")
        }
    }

    private val videoUris = mutableListOf<Uri>()

    fun play(context: Context, from: Long = 0) {
        val videoPartIndex = videoParts.indexOfFirst { it.from <= from && from <= it.to }
        val seekTo = if (videoPartIndex != -1) from - videoParts[videoPartIndex].from else 0
        runCaching(
            context = context,
            initialIndex = videoPartIndex ?: 0,
            onReadyToPlay = {
                videoUris.clear()
                videoUris.addAll(videoPartNames.map { FileUtils.getCachedVideoByName(context, it) }.map { Uri.fromFile(it) })
                playVideo(videoPartIndex, seekTo.toInt())
                getMediaPlayerPlaying().setOnCompletionListener {
                    currentVideoIndex++
                    if (currentVideoIndex < videoParts.size) {
                        playNextVideo()
                    }
                }
                getMediaPlayerPaused().setOnCompletionListener {
                    currentVideoIndex++
                    if (currentVideoIndex < videoParts.size) {
                        playNextVideo()
                    }
                }
            }
        )
    }

    private fun playVideo(index: Int, from: Int = 0) {
        getMediaPlayerPlaying().setDataSource(context, videoUris[index])
        if (from > 0) {
            getMediaPlayerPlaying().seekTo(from)
        }
        getMediaPlayerPlaying().prepareAsync()
        getMediaPlayerPlaying().setOnPreparedListener { mp -> getMediaPlayerPlaying().start() }

        // Preload the next video after playing the current one
        if (index + 1 < videoParts.size) {
            preloadNextVideo(index + 1)
        }
    }

    private fun preloadNextVideo(index: Int) {
        val videoUri = videoUris[index]
        val mediaPlayer = getMediaPlayerPaused()

        mediaPlayer.setDataSource(context, videoUri)
        mediaPlayer.prepareAsync()
    }

    private fun playNextVideo() {
        // Khi video hiện tại kết thúc, chuyển sang video đã preload
        if (currentVideoIndex < videoUris.size) {
            // change the display of the SurfaceView
            getMediaPlayerPlaying().setDisplay(null)
            getMediaPlayerPaused().setDisplay(videoView1.holder)
            getMediaPlayerPaused().start()
            isVideoView1Playing = !isVideoView1Playing
        }

//        // Preload video tiếp theo (nếu có)
//        if (currentVideoIndex + 1 < videoParts.size) {
//            preloadNextVideo(currentVideoIndex + 1)
//        }
    }

    private fun getMediaPlayerPlaying(): MediaPlayer {
        return if (isVideoView1Playing) mediaPlayer1 else mediaPlayer2
    }

    private fun getMediaPlayerPaused(): MediaPlayer {
        return if (isVideoView1Playing) mediaPlayer2 else mediaPlayer1
    }

    private fun runCaching(
        context: Context,
        initialIndex: Int = 0,
        onReadyToPlay: () -> Unit,
        onCacheFailed: (Int) -> Unit = {}
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            var loopCount = 0
            var index = initialIndex
            val maximumTry = 10
            while (loopCount < videoParts.size) {
                var isCachedSuccess = false
                var tryCount = 0
                while (!isCachedSuccess && tryCount < maximumTry) {
                    isCachedSuccess = FileUtils.saveVideoToCacheDir(context, videoParts[index].url, videoPartNames[index])
                    tryCount++
                }
                if (isCachedSuccess) {
                    if (index == initialIndex) {
                        withContext(Dispatchers.Main) {
                            onReadyToPlay()
                        }
                    }
                    Log.d("VideoPlayer", "Cached video part $index")
                    index = (index + 1) % videoParts.size
                } else {
                    withContext(Dispatchers.Main) {
                        onCacheFailed(index)
                    }
                    Log.d("VideoPlayer", "Failed to cache video part $index")
                    break
                }
                loopCount++
            }
        }
    }
}