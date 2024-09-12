package com.kma.musicplayerv2.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.extension.showDialog

object PermissionUtils {

    private val readAudioPermissionsToRequest = mutableListOf<String>().apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.READ_MEDIA_AUDIO)
        } else {
            add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private val readPhotoPermissionsToRequest = mutableListOf<String>().apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    fun isReadAudioPermissionGranted(context: Context): Boolean {
        return readAudioPermissionsToRequest.all {
            context.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun isReadPhotoPermissionGranted(context: Context): Boolean {
        return readPhotoPermissionsToRequest.all {
            context.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun isWriteExternalStoragePermissionGranted(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }
        return true
    }

    fun isMicrophoneAndStoragePermissionGranted(context: Context) : Boolean{
        return isWriteExternalStoragePermissionGranted(context)
                && isMicrophonePermissionGranted(context)
    }

    fun isMicrophonePermissionGranted(context: Context) : Boolean{
        return context.checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
    }

    fun requestReadAudioPermission(activity: Activity, requestCode: Int = 1000) {
        requestPermissions(activity, readAudioPermissionsToRequest.toTypedArray(), requestCode)
    }

    fun requestReadPhotoPermission(activity: Activity, requestCode: Int = 1000) {
        requestPermissions(activity, readPhotoPermissionsToRequest.toTypedArray(), requestCode)
    }

    private fun requestPermissions(
        activity: Activity,
        permissions: Array<String>,
        requestCode: Int = 1000
    ) {
        val showRationale = permissions.any {
            ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
        }

        if (showRationale) {
            showGotoSettingDialog(activity, requestCode)
        } else {
            ActivityCompat.requestPermissions(
                activity,
                permissions,
                requestCode
            )
        }
    }

    private fun showGotoSettingDialog(activity: Activity, requestCode: Int = 1000) {
        activity.showDialog(
            title = activity.getString(R.string.grant_permission),
            message = activity.getString(R.string.please_grant_all_permissions),
            textOfPositiveButton = activity.getString(R.string.go_to_setting),
            positiveButtonFunction = {
                goToSetting(activity, requestCode)
            }
        )
    }

    private fun goToSetting(activity: Activity, requestCode: Int = 1000) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity.packageName, null)
        intent.data = uri
        activity.startActivityForResult(intent, requestCode)
    }
}