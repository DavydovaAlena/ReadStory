package ru.adavydova.booksmart.data.remote.repository

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import androidx.media3.common.MimeTypes
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.adavydova.booksmart.domain.repository.DownloadBookFromWebRepository
import javax.inject.Inject

class DownloadBookFromWebRepositoryImpl(
   private val context: Context
): DownloadBookFromWebRepository {
    private val downloadManager = context.getSystemService(DownloadManager::class.java)
    override fun downloadFile(url: String, title:String, mimiType:String): Long {
        val request = DownloadManager.Request(url.toUri())
            .setMimeType(mimiType)
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle(title)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title)
        return downloadManager.enqueue(request)
    }
}