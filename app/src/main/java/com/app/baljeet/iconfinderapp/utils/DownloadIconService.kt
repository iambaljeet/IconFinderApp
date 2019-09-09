package com.app.baljeet.iconfinderapp.utils

import android.app.DownloadManager
import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.net.Uri

class DownloadIconService : IntentService("DownloadIconService") {

    override fun onHandleIntent(intent: Intent?) {
        val downloadPath = intent?.getStringExtra(Constants.DOWNLOAD_PATH)
        val destinationPath = intent?.getStringExtra(Constants.DESTINATION_PATH)
        startDownload(downloadPath, destinationPath)
    }

    private fun startDownload(downloadPath: String?, destinationPath: String?) {
        val uri = Uri.parse(downloadPath)
        val request = DownloadManager.Request(uri)
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setTitle("Downloading icon")
        request.setDestinationInExternalPublicDir(
            destinationPath,
            uri.lastPathSegment
        )
        (getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).enqueue(request) // This will start downloading
    }

    companion object {
        fun getDownloadService(
            context: Context,
            downloadPath: String?,
            destinationPath: String
        ): Intent {
            return Intent(context, DownloadIconService::class.java)
                .putExtra(Constants.DOWNLOAD_PATH, downloadPath)
                .putExtra(Constants.DESTINATION_PATH, destinationPath)
        }
    }
}