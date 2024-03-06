package ru.adavydova.booksmart.data.local.repository

import org.readium.r2.shared.util.AbsoluteUrl
import ru.adavydova.booksmart.data.local.book.DownloadDao
import ru.adavydova.booksmart.domain.model.readium_book.Download
import ru.adavydova.booksmart.domain.repository.DownloadReadiumBookRepository
import javax.inject.Inject

class DownloadReadiumBookRepositoryImpl @Inject constructor(
    private val downloadDao: DownloadDao,
    private val type: Download.Type
): DownloadReadiumBookRepository
{
    override suspend fun all(): List<Download> {
        return downloadDao.getDownloads(type)
    }

    override suspend fun insert(id: String, cover: AbsoluteUrl?) {
        return downloadDao.insert(
            Download(
                type = type,
                id = id,
                cover = cover?.toString(),

            )
        )
    }

    override suspend fun remove(id: String) {
       downloadDao.delete(
           id = id,
           type = type)
    }

    override suspend fun getCover(id: String): AbsoluteUrl? {
       return downloadDao.get(
           id = id,
           type = type
       )?.cover?.let { AbsoluteUrl(it) }
    }
}