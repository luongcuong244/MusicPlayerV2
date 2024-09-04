package com.kma.musicplayerv2

import android.app.Application
import com.kma.musicplayerv2.utils.SharePrefUtils

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        SharePrefUtils.init(this)
    }
}