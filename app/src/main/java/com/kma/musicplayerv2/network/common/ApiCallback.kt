package com.kma.musicplayerv2.network.common

interface ApiCallback<T> {
    fun onSuccess(data: T?)
    fun onFailure(message: String)
}