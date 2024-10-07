package com.kma.musicplayerv2.ui.screen.personal

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.kma.musicplayerv2.BuildConfig
import com.kma.musicplayerv2.ui.core.BaseFragment
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.databinding.FragmentPersonalBinding
import com.kma.musicplayerv2.globalstate.CurrentUser
import com.kma.musicplayerv2.ui.dialog.ChangeUserNameDialog
import com.kma.musicplayerv2.ui.dialog.RatingDialog
import com.kma.musicplayerv2.ui.screen.addwidget.AddWidgetActivity
import com.kma.musicplayerv2.ui.screen.hiddensong.HiddenSongActivity
import com.kma.musicplayerv2.ui.screen.language.LanguageActivity
import com.kma.musicplayerv2.ui.screen.policy.PolicyActivity
import com.kma.musicplayerv2.ui.screen.signin.SigninActivity
import com.kma.musicplayerv2.utils.PermissionUtils
import com.kma.musicplayerv2.utils.SharePrefUtils

class PersonalFragment : BaseFragment<FragmentPersonalBinding>() {
    private val REQUEST_CODE_PICK_IMAGE = 100

    override fun getContentView(): Int = R.layout.fragment_personal

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setupListeners()
    }

    private fun initView() {
        binding.tvName.text = CurrentUser.getUser()!!.userName
        Glide.with(requireContext())
            .load(CurrentUser.getUser()!!.avatar)
            .placeholder(R.drawable.bg_default_avatar)
            .into(binding.ivAvatar)
    }

    private fun setupListeners() {
        binding.ivAvatar.setOnClickListener {
            if (PermissionUtils.isReadPhotoPermissionGranted(requireActivity())) {
                openGallery()
            } else {
                PermissionUtils.requestReadPhotoPermission(requireActivity())
            }
        }
        binding.tvName.setOnClickListener {
            val changeUserNameDialog = ChangeUserNameDialog(
                userName = binding.tvName.text.toString(),
                onUserNameChanged = {
                    binding.tvName.text = it
                }
            )
            changeUserNameDialog.show(
                childFragmentManager,
                ChangeUserNameDialog::class.java.simpleName
            )
        }
        binding.llHiddenSong.setOnClickListener {
            showActivity(HiddenSongActivity::class.java)
        }
        binding.llAddWidget.setOnClickListener {
            showActivity(AddWidgetActivity::class.java)
        }
        binding.llLanguage.setOnClickListener {
            showActivity(LanguageActivity::class.java)
        }
        binding.llShareApp.setOnClickListener {
            shareApp()
        }
        binding.llRateApp.setOnClickListener {
            showRatingDialog()
        }
        binding.llPolicy.setOnClickListener {
            showActivity(PolicyActivity::class.java)
        }
        binding.rlSignOut.setOnClickListener {
            CurrentUser.setUser(null)
            SharePrefUtils.saveAccessToken("")
            SharePrefUtils.saveRefreshToken("")
            showActivity(SigninActivity::class.java)
            requireActivity().finishAffinity()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    private fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
        var shareMessage = getString(R.string.app_name)
        shareMessage =
            "$shareMessage \nhttps://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        startActivity(Intent.createChooser(shareIntent, "Share to"))
    }

    private fun showRatingDialog() {
        RatingDialog(requireActivity()).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            binding.ivAvatar.setImageURI(uri)
        }
    }
}