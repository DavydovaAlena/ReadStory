package ru.adavydova.booksmart.domain.repository

import androidx.media3.common.MimeTypes

interface DownloadBookFromWebRepository {
    fun downloadFile(url:String, title:String,  mimiType: String):Long
}