package com.kma.musicplayerv2.ui.screen.signup

import android.os.Bundle
import com.kma.musicplayerv2.ui.core.BaseActivity
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.databinding.ActivitySignupBinding

class SignupActivity : BaseActivity<ActivitySignupBinding>() {
    override fun getContentView(): Int = R.layout.activity_signup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        binding.rlBack.setOnClickListener {
            finish()
        }
    }
}