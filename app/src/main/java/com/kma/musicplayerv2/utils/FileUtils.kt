package com.kma.musicplayerv2.utils

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import com.kma.musicplayerv2.model.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object FileUtils {
    const val APP_FOLDER_NAME = "MusicPlayerV2"

    private val localSongs = mutableListOf<Song>()

    private fun getMediaDurationInSeconds(context: Context, path: String): Long {
        try {
            val file = File(path)
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(context, Uri.fromFile(file))
            val time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            val timeInMillisec = time?.toLong() ?: 0
            retriever.release()
            return timeInMillisec / 1000
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    fun saveSongToCacheDir(
        context: Context,
        song: Song,
        onDownloadSuccess: (File) -> Unit,
        onDownloadFailed: () -> Unit
    ) {
        // Save online song to cache dir
        val fileName = generateFileName(song)
        saveMp3ToCacheDir(
            context,
            song.path,
            fileName,
            onDownloadSuccess,
            onDownloadFailed
        )
    }

    fun generateFileName(song: Song): String {
        return song.title + "-" + song.id + ".mp3"
    }

    fun getDownloadedSongs(context: Context): List<File> {
        val directoryPath = getDirectoryPath(context) ?: return emptyList()
        val directory = File(directoryPath)
        if (!directory.exists()) {
            return emptyList()
        }
        return directory.listFiles()?.toList() ?: emptyList()
    }

    fun getSongIdFromFile(file: File): Long {
        val fileName = file.name
        val idString = fileName.substringAfterLast("-").substringBeforeLast(".mp3")
        return idString.toLong()
    }

    private fun saveMp3ToCacheDir(
        context: Context,
        url: String,
        fileName: String,
        onDownloadSuccess: (File) -> Unit,
        onDownloadFailed: () -> Unit,
    ) {
        try {
            val file = File(context.cacheDir, fileName)
            if (file.exists()) {
                onDownloadSuccess.invoke(file)
                return
            }

            val client = OkHttpClient()
            val request = Request.Builder()
                .url(url)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    Log.d("FileUtils", "onFailure: ${e.message}")
                    CoroutineScope(Dispatchers.Main).launch {
                        onDownloadFailed.invoke()
                    }
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        throw IOException("Unexpected code $response")
                    }
                    val responseBody = response.body
                    if (responseBody != null) {
                        val inputStream = responseBody.byteStream()
                        val outputStream = FileOutputStream(file)
                        val buffer = ByteArray(4096)
                        var bytesRead: Int
                        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                            outputStream.write(buffer, 0, bytesRead)
                        }
                        outputStream.close()
                        inputStream.close()
                        CoroutineScope(Dispatchers.Main).launch {
                            onDownloadSuccess.invoke(file)
                        }
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            onDownloadFailed.invoke()
        }
    }

    fun getUriFromFile(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(context, context.packageName + ".provider", file)
    }

    fun getAudioMediaContentUri(context: Context, filePath: String): Uri? {
        val contentResolver: ContentResolver = context.contentResolver
        val uri: Uri = MediaStore.Files.getContentUri("external")

        val projection = arrayOf(MediaStore.Files.FileColumns._ID)
        val selection = "${MediaStore.Files.FileColumns.DATA} = ?"
        val selectionArgs = arrayOf(filePath)
        val sortOrder = null // You can specify sorting order if needed

        contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)
                val id = cursor.getLong(columnIndex)
                val contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                return Uri.withAppendedPath(contentUri, id.toString())
            }
        }
        return null
    }

    fun addAudioToMediaStore(context: Context, filePath: String) {
        val current = System.currentTimeMillis()
        val contentValues = ContentValues().apply {
            put(MediaStore.Audio.Media.DISPLAY_NAME, File(filePath).name)
            put(MediaStore.Audio.Media.MIME_TYPE, "audio/*")
            put(MediaStore.Audio.Media.DATA, filePath)
            put(MediaStore.Audio.Media.DATE_ADDED, current)
        }
        val contentResolver = context.contentResolver
        contentResolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues)
    }

    fun getDirectoryPath(context: Context): String? {
        return try {
            val file = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC),
                APP_FOLDER_NAME
            )
            if (!file.exists()) {
                file.mkdirs()
            }
            file.absolutePath
        } catch (e: Exception) {
            context.filesDir.absolutePath // use internal storage if external storage is not available
        }
    }

    fun renameFile(oldFilePath: String, newName: String): File? {
        val oldFile = File(oldFilePath)
        return if (oldFile.exists()) {
            val parentDirectory = oldFile.parent
            val extension = getExtension(oldFile.name)
            val newFilePath = parentDirectory + File.separator + newName + extension
            val newFile = File(newFilePath)
            if(newFile.exists()){
                null
            } else {
                oldFile.renameTo(newFile)
                newFile
            }
        } else {
            null
        }
    }

    fun deleteFile(path: String) : Boolean {
        val file = File(path)
        if(!file.exists()){
            return false
        }
        return File(path).delete()
    }

    private fun getExtension(fileName: String): String {
        val dotIndex = fileName.lastIndexOf(".")
        return if (dotIndex > 0 && dotIndex < fileName.length - 1) {
            fileName.substring(dotIndex)
        } else ""
    }
}