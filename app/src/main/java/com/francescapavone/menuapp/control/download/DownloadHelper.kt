package com.francescapavone.menuapp.control.download

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import java.io.File

class DownloadHelper : AppCompatActivity() {
    fun download(url: String, fileName: String, downloadManager: DownloadManager, context: Context) {
        val fileLink = Uri.parse(url)
        val request = DownloadManager.Request(fileLink)

        try {
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
                .setMimeType("application/pdf")
                .setAllowedOverRoaming(false)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setTitle("$fileName.pdf")
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    File.separator + fileName + ".pdf"
                )

            downloadManager.enqueue(request)
            toast("Downloading",context) /* success */
        } catch (e: Exception) {
            toast("Failed Download",context) /* failure */
        }
    }

    private fun toast(message: String,context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

}