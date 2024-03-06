package ru.adavydova.booksmart.domain.repository

import org.readium.r2.shared.util.AbsoluteUrl
import ru.adavydova.booksmart.domain.model.readium_book.Download

interface DownloadReadiumBookRepository {

    suspend fun all(): List<Download>
    suspend fun insert(
        id: String,
        cover: AbsoluteUrl?
    )
    suspend fun remove(id:String)
    suspend fun getCover(id:String): AbsoluteUrl?
}