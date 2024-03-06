package ru.adavydova.booksmart.domain.usecase.google_books_remote

import android.content.Context
import androidx.media3.common.MimeTypes
import ru.adavydova.booksmart.data.remote.repository.DownloadBookFromWebRepositoryImpl
import ru.adavydova.booksmart.domain.repository.DownloadBookFromWebRepository

class DownloadBookUseCase {
    operator fun invoke(context: Context, url:String, title:String, mimeTypes: String): Long {
        val downloader = DownloadBookFromWebRepositoryImpl(context)
        return downloader.downloadFile(url, title, mimeTypes)
    }
}