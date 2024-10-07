package com.kma.musicplayerv2.ui.screen.signin

import android.os.Bundle
import android.util.Log
import com.kma.musicplayerv2.ui.core.BaseActivity
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.databinding.ActivitySigninBinding
import com.kma.musicplayerv2.globalstate.CurrentUser
import com.kma.musicplayerv2.model.User
import com.kma.musicplayerv2.network.common.ApiCallback
import com.kma.musicplayerv2.network.retrofit.model.LoginRequest
import com.kma.musicplayerv2.network.retrofit.model.LoginResponse
import com.kma.musicplayerv2.network.retrofit.repository.AuthRepository
import com.kma.musicplayerv2.network.retrofit.repository.UserRepository
import com.kma.musicplayerv2.ui.screen.main.MainActivity
import com.kma.musicplayerv2.ui.screen.signup.SignupActivity
import com.kma.musicplayerv2.utils.SharePrefUtils
import retrofit2.Call
import retrofit2.Callback

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
        binding.rlContinue.setOnClickListener {
            AuthRepository.login(
                LoginRequest(
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()
                ),
                object : Callback<LoginResponse> {
                    override fun onResponse(call: Call<LoginResponse>, response: retrofit2.Response<LoginResponse>) {
                        if (response.isSuccessful) {
                            SharePrefUtils.saveAccessToken(response.body()?.accessToken ?: "")
                            SharePrefUtils.saveRefreshToken(response.body()?.refreshToken ?: "")
                            response.body()?.user?.let {
                                CurrentUser.setUser(it.toUser())
                            }
                            showActivity(MainActivity::class.java)
                        } else {
                            Log.d("SigninActivityy", "Error: ${response.errorBody()?.string()}")
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Log.d("SigninActivityy", "Error: ${t.message}")
                    }
                }
            )
        }
    }
}