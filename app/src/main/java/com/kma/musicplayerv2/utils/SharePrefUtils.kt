package com.kma.musicplayerv2.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object SharePrefUtils {
    private var mSharePref: SharedPreferences? = null
    fun init(context: Context) {
        if (mSharePref == null) {
            mSharePref = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        }
    }

    fun saveSongIds(songIds: List<String>): Boolean {
        val editor = mSharePref!!.edit()
        editor.putString("songIds", songIds.joinTo(StringBuilder(), ",").toString())
        return editor.commit()
    }

    fun getSongIds(): List<String> {
        val songIds = mSharePref!!.getString("songIds", "")
        if (songIds.isNullOrEmpty()) {
            return emptyList()
        }
        return songIds.split(",").map { it }
    }

    fun saveCurrentSongIndex(index: Int): Boolean {
        val editor = mSharePref!!.edit()
        editor.putInt("currentSongIndex", index)
        return editor.commit()
    }

    fun getCurrentSongIndex(): Int {
        return mSharePref!!.getInt("currentSongIndex", -1)
    }

    fun saveAccessToken(accessToken: String): Boolean {
        val editor = mSharePref!!.edit()
        editor.putString("accessToken", accessToken)
        return editor.commit()
    }

    fun getAccessToken(): String? {
        return mSharePref!!.getString("accessToken", null)
    }

    fun saveRefreshToken(refreshToken: String): Boolean {
        val editor = mSharePref!!.edit()
        editor.putString("refreshToken", refreshToken)
        return editor.commit()
    }

    fun getRefreshToken(): String? {
        return mSharePref!!.getString("refreshToken", null)
    }
}