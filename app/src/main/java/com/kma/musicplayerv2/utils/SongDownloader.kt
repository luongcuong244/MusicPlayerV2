package com.kma.musicplayerv2.utils

import android.content.Context
import com.kma.musicplayerv2.model.Song
import java.io.File

object SongDownloader {
    fun downloadSong(
        context: Context, song: Song, onDownloadSuccess: (File) -> Unit,
        onDownloadFailed: () -> Unit
    ) {
        FileUtils.saveSongToCacheDir(
            context,
            song,
            onDownloadSuccess,
            onDownloadFailed
        )
    }
}