package com.francescapavone.menuapp.control

import android.app.DownloadManager
import android.content.Context
import com.francescapavone.menuapp.control.download.DownloadHelper

class QrControl(var context: Context, var downloadManager: DownloadManager) {
    fun downloadMenu(code: String){
        val downloadHelper = DownloadHelper()
        downloadHelper.download(code, "Menu", downloadManager, context)
    }

}