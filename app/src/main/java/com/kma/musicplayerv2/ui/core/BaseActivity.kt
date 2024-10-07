package com.kma.musicplayerv2.ui.core

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.service.PlaySongService
import com.kma.musicplayerv2.service.ServiceController
import com.kma.musicplayerv2.ui.customview.BottomMiniAudioPlayer
import com.kma.musicplayerv2.utils.SharePrefUtils
import com.kma.musicplayerv2.utils.SystemUtil

abstract class BaseActivity<DB : ViewDataBinding> : AppCompatActivity(), ServiceConnection {

    lateinit var binding: DB
    private var currentApiVersion = 0

    private var _songService: PlaySongService? = null
    val songService: PlaySongService?
        get() = _songService

    val mBound = MutableLiveData<Boolean>().apply {
        value = false
    }
    override fun onServiceConnected(className: ComponentName, service: IBinder) {
        val binder = service as PlaySongService.LocalBinder
        _songService = binder.getService()
        mBound.value = true
    }

    override fun onServiceDisconnected(className: ComponentName) {
         mBound.value = false
    }

    fun isServiceBound(): Boolean {
        return mBound.value ?: false
    }

    abstract fun getContentView(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemUtil.setLocale(this)
        currentApiVersion = Build.VERSION.SDK_INT
        val flags: Int = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            window.decorView.systemUiVisibility = flags
            val decorView: View = window.decorView
            decorView
                .setOnSystemUiVisibilityChangeListener { visibility ->
                    if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                        decorView.systemUiVisibility = flags
                    }
                }
        }
        binding = DataBindingUtil.setContentView(this, getContentView())

        Log.d("CHECK_ACTIVITY", "onCreate: ${javaClass.simpleName}")
    }

    override fun onStart() {
        super.onStart()
        val songIds = SharePrefUtils.getSongIds()
        if (songIds.isNullOrEmpty()) {
            return
        }
        if (!ServiceController.isServiceRunning(this, PlaySongService::class.java)) {
            startService(Intent(this, PlaySongService::class.java))
        }
        bindService(
            Intent(this, PlaySongService::class.java),
            this,
            BIND_AUTO_CREATE
        )
    }

    override fun onStop() {
        super.onStop()
        if (mBound.value == true) {
            unbindService(this)
            mBound.value = false
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

    fun showActivity(clazz: Class<*>, bundle: Bundle? = null) {
        val intent = Intent(this, clazz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    fun showActivityForResult(clazz: Class<*>, requestCode: Int, bundle: Bundle? = null) {
        val intent = Intent(this, clazz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivityForResult(intent, requestCode)
    }

    override fun onResume() {
        super.onResume()
        val bottomMiniPlayer =
            binding.root.findViewById<FrameLayout>(R.id.bottom_mini_player)

        if (bottomMiniPlayer != null && ServiceController.isServiceRunning(this, PlaySongService::class.java)) {
            bottomMiniPlayer.visibility = View.VISIBLE
            mBound.observe(this) {
                if (it) {
                    songService?.let {
                        if (it.bottomMiniAudioPlayer == null) {
                            it.bottomMiniAudioPlayer = BottomMiniAudioPlayer(this)
                            it.bottomMiniAudioPlayer!!.activity = this
                            it.bottomMiniAudioPlayer!!.initView(it)
                        }
                        it.bottomMiniAudioPlayer!!.activity = this
                        it.bottomMiniAudioPlayer!!.parent?.let { parent ->
                            (parent as ViewGroup).removeView(it.bottomMiniAudioPlayer!!)
                        }
                        bottomMiniPlayer.addView(it.bottomMiniAudioPlayer)
                    }
                }
            }
        } else {
            bottomMiniPlayer?.visibility = View.GONE
        }
    }
}