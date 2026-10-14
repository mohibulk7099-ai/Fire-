package com.example.service

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

object VideoDownloader {

    fun downloadVideoWithDownloadManager(
        context: Context,
        videoUrl: String,
        fileName: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val uri = Uri.parse(videoUrl)

            val request = DownloadManager.Request(uri).apply {
                setTitle(fileName)
                setDescription("AI Video Maker MP4 Download")
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                setAllowedOverMetered(true)
                setAllowedOverRoaming(true)
            }

            downloadManager.enqueue(request)
            Toast.makeText(context, "डाउनलोड शुरू हो गया है / Download started", Toast.LENGTH_SHORT).show()
            onSuccess(fileName)
        } catch (e: Exception) {
            e.printStackTrace()
            onError(e.localizedMessage ?: "Download failed")
        }
    }

    suspend fun downloadDirectToFile(
        context: Context,
        videoUrl: String,
        destinationFile: File,
        onProgress: (Float) -> Unit
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val url = URL(videoUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 15000
            connection.readTimeout = 15000
            connection.connect()

            val fileLength = connection.contentLength
            val input = connection.inputStream
            val output = FileOutputStream(destinationFile)

            val buffer = ByteArray(4096)
            var total: Long = 0
            var count: Int

            while (input.read(buffer).also { count = it } != -1) {
                total += count
                if (fileLength > 0) {
                    onProgress(total.toFloat() / fileLength.toFloat())
                }
                output.write(buffer, 0, count)
            }

            output.flush()
            output.close()
            input.close()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
