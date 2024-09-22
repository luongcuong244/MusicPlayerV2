package com.kma.musicplayerv2.ui.screen.signup

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.kma.musicplayerv2.ui.core.BaseActivity
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.databinding.ActivitySignupBinding
import com.kma.musicplayerv2.model.User
import com.kma.musicplayerv2.network.common.ApiCallback
import com.kma.musicplayerv2.network.retrofit.api.AuthApi
import com.kma.musicplayerv2.network.retrofit.model.RegisterRequest
import com.kma.musicplayerv2.network.retrofit.repository.AuthRepository
import com.kma.musicplayerv2.network.retrofit.repository.UserRepository
import com.kma.musicplayerv2.ui.screen.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        binding.rlContinue.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val userName = binding.etUsername.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty() && userName.isNotEmpty()) {
                AuthRepository.register(
                    RegisterRequest(email, userName, password, password),
                    object : Callback<Boolean> {
                        override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                            if (response.isSuccessful) {
                                finish()
                                Toast.makeText(this@SignupActivity, "Register successfully", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@SignupActivity, response.errorBody()?.string() ?: "Unknown error", Toast.LENGTH_SHORT).show()
                                Log.d("SignupActivityy", "Error: ${response.errorBody()?.string()}")
                            }
                        }

                        override fun onFailure(call: Call<Boolean>, t: Throwable) {
                            Toast.makeText(this@SignupActivity, t.message ?: "Unknown error", Toast.LENGTH_SHORT).show()
                            Log.d("SignupActivityy", "Error: ${t.message}")
                        }
                    }
                )
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}