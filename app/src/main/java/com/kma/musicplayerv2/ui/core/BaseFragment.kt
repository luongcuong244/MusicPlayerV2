package com.kma.musicplayer.ui.screen.core

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.kma.musicplayerv2.ui.core.BaseActivity
import com.kma.musicplayerv2.utils.SystemUtil

abstract class BaseFragment<DB : ViewDataBinding> : Fragment() {
    lateinit var binding: DB

    abstract fun getContentView(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        SystemUtil.setLocale(requireContext())
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        var rootView: View? = null
        val resId = getContentView()
        if (resId > 0) {
            binding = DataBindingUtil.inflate(
                inflater,
                getContentView(),
                container,
                false
            )
            rootView = binding.root
        }
        return rootView
    }

    fun getBaseActivity(): BaseActivity<*> {
        return requireActivity() as BaseActivity<*>
    }

    fun showActivity(clazz: Class<*>, bundle: Bundle? = null) {
        val intent = Intent(requireContext(), clazz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    fun showActivityForResult(clazz: Class<*>, requestCode: Int, bundle: Bundle? = null) {
        val intent = Intent(requireContext(), clazz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivityForResult(intent, requestCode)
    }

//    override fun onResume() {
//        super.onResume()
//        val bottomMiniPlayer =
//            binding.root.findViewById<FrameLayout>(R.id.bottom_mini_player)
//
//        if (bottomMiniPlayer != null && ServiceController.isServiceRunning(requireContext(), PlaySongService::class.java)) {
//            getBaseActivity().mBound.observe(this) {
//                if (it) {
//                    getBaseActivity().songService?.let {
//                        if (it.bottomMiniAudioPlayer == null) {
//                            it.bottomMiniAudioPlayer = BottomMiniAudioPlayer(requireActivity())
//                            it.bottomMiniAudioPlayer!!.activity = getBaseActivity()
//                            it.bottomMiniAudioPlayer!!.initView(it)
//                        }
//                        it.bottomMiniAudioPlayer!!.activity = getBaseActivity()
//                        it.bottomMiniAudioPlayer!!.parent?.let { parent ->
//                            (parent as ViewGroup).removeView(it.bottomMiniAudioPlayer!!)
//                        }
//                        bottomMiniPlayer.addView(it.bottomMiniAudioPlayer)
//                    }
//                }
//            }
//        } else {
//            bottomMiniPlayer?.visibility = View.GONE
//        }
//    }
}