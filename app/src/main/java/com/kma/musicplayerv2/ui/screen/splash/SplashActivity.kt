package com.kma.musicplayerv2.ui.screen.splash

import android.os.Bundle
import android.os.Handler
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.databinding.ActivitySplashBinding
import com.kma.musicplayerv2.globalstate.CurrentUser
import com.kma.musicplayerv2.model.User
import com.kma.musicplayerv2.network.common.ApiCallback
import com.kma.musicplayerv2.network.retrofit.repository.UserRepository
import com.kma.musicplayerv2.ui.core.BaseActivity
import com.kma.musicplayerv2.ui.screen.main.MainActivity
import com.kma.musicplayerv2.ui.screen.signin.SigninActivity

class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    override fun getContentView(): Int = R.layout.activity_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UserRepository.getUserInfo(
            object : ApiCallback<User> {
                override fun onSuccess(result: User?) {
                    result?.let {
                        CurrentUser.setUser(it)
                        showNextActivity(MainActivity::class.java)
                    }
                }

                override fun onFailure(message: String) {
                    showNextActivity(SigninActivity::class.java)
                }
            }
        )
    }

    private fun showNextActivity(activity: Class<*>) {
        Handler().postDelayed({
            showActivity(activity)
            finish()
        }, 3000)
    }
}