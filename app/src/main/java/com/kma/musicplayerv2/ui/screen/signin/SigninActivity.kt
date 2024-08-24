package com.kma.musicplayerv2.ui.screen.signin

import android.os.Bundle
import com.kma.musicplayerv2.ui.core.BaseActivity
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.databinding.ActivitySigninBinding
import com.kma.musicplayerv2.ui.screen.signup.SignupActivity

class SigninActivity : BaseActivity<ActivitySigninBinding>() {
    override fun getContentView(): Int = R.layout.activity_signin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        binding.rlSignUp.setOnClickListener {
            showActivity(SignupActivity::class.java)
        }
    }
}