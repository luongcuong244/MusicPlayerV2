package com.kma.musicplayerv2.ui.screen.policy

import android.os.Bundle
import android.webkit.WebSettings
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.databinding.ActivityPolicyBinding
import com.kma.musicplayerv2.ui.core.BaseActivity

class PolicyActivity : BaseActivity<ActivityPolicyBinding>() {

    companion object {
        const val PRIVACY_POLICY_LINK = "https://firebasestorage.googleapis.com/v0/b/asa134-cast-to-tv.appspot.com/o/Privacy-Policy.html?alt=media&token=2160578b-06d3-4837-8cb7-ece49923b019"
    }

    override fun getContentView(): Int = R.layout.activity_policy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val webSettings: WebSettings = binding.webview.settings
        webSettings.javaScriptEnabled = true
        binding.webview.loadUrl(PRIVACY_POLICY_LINK)
    }
}