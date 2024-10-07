package com.kma.musicplayerv2.globalstate

import com.kma.musicplayerv2.model.User

object CurrentUser {

    private var user: User? = null

    fun getUser(): User? {
        return user
    }

    fun setUser(user: User?) {
        CurrentUser.user = user
    }
}