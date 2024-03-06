package ru.adavydova.booksmart.data.local.repository

import android.app.Application
import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import org.readium.r2.shared.publication.Locator
import org.readium.r2.shared.publication.Publication
import org.readium.r2.shared.publication.indexOfFirstWithHref
import org.readium.r2.shared.util.Url
import org.readium.r2.shared.util.mediatype.MediaType
import ru.adavydova.booksmart.data.local.book.ReadiumBooksDao
import ru.adavydova.booksmart.domain.model.readium_book.Bookmark
import ru.adavydova.booksmart.domain.model.readium_book.Highlight
import ru.adavydova.booksmart.domain.model.readium_book.ReadiumBook
import ru.adavydova.booksmart.domain.model.readium_book.TemporaryOpeningBook
import ru.adavydova.booksmart.domain.repository.ReadiumBooksRepository
import java.io.File
import java.util.Date
import java.util.Properties
import javax.inject.Inject

class ReadiumBooksRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val bookDao: ReadiumBooksDao,
) : ReadiumBooksRepository {

    override suspend fun updateBookOrderByTimeOpening(bookId: Long, progress: String, time:Long) {
        Log.d("UPD", progress.toString())
        bookDao.updateTimeOpeningBook(
            progress = progress,
            bookId = bookId,
            time = time
        )
    }

    override suspend fun updateHighlightAnnotationStyle(
        id: Long,
        style: Highlight.Style,
        tint: Int
    ) {
        bookDao.updateHighlightStyle(id, style, tint)
    }

    override suspend fun deleteBookOrderByTimeOpening(bookId: Long) {
        return bookDao.deleteTimeOpeningBook(bookId)
    }

    override fun getBooksOrderByTimeOpening(): Flow<List<TemporaryOpeningBook>> {
       return bookDao.getReadiumBooksSortedByOrderingTime()
    }

    override suspend fun insertBookOrderByTimeOpening(temporaryOpeningBook: TemporaryOpeningBook) {
       return bookDao.insertTimeOpeningBook(temporaryOpeningBook)
    }

    override suspend fun computeStorageDir(): File {
        val properties = Properties()
        val inputStream = context.assets.open("configs/config.properties")
        properties.load(inputStream)
        val useExternalFileDir = properties.getProperty("useExternalFileDir", "false")!!.toBoolean()

        return File(
            if (useExternalFileDir) {
                context.getExternalFilesDir(null)?.path + "/"
            } else {
                context.filesDir?.path + "/"
            }
        )
    }

    override suspend fun get(id: Long): ReadiumBook? {
       return bookDao.get(id)
    }

    override suspend fun saveProgression(locator: Locator, bookId: Long) {
        bookDao.saveProgression(
            locator = locator.toJSON().toString(),
            id = bookId
        )
    }

    override fun books(): Flow<List<ReadiumBook>> {
        return bookDao.getAllBooks()
    }

    override suspend fun insertBookmark(
        bookId: Long,
        publication: Publication,
        locator: Locator
    ): Long {
        val resource = publication.readingOrder.indexOfFirstWithHref(locator.href)!!
        return bookDao.insertBookmark(
            Bookmark(
                id = bookId,
                creation = Date().time,
                bookId = bookId,
                resourceIndex = resource.toLong(),
                resourceHref = locator.href.toString(),
                resourceType = locator.mediaType.toString(),
                resourceTitle = locator.title.orEmpty(),
                location = locator.locations.toJSON().toString(),
                locatorText = Locator.Text().toJSON().toString()
                )
        )
    }

    override fun bookmarksForBook(bookId: Long): Flow<List<Bookmark>> {
       return bookDao.getBookmarksForBook(bookId)
    }

    override suspend fun deleteBookmark(bookmarkId: Long) {
        return bookDao.deleteBookmark(bookmarkId)
    }

    override suspend fun highlightById(id: Long): Highlight? {
        return bookDao.getHighlightById(id)
    }

    override fun highlightsForBook(bookId: Long): Flow<List<Highlight>> {
        return bookDao.getHighlightsForBook(bookId)
    }

    override suspend fun addHighlight(
        bookId: Long,
        style: Highlight.Style,
        tint: Int,
        locator: Locator,
        annotation: String
    ): Long {
        return bookDao.insertHighlight(
            Highlight(
                bookId = bookId,
                style = style,
                tint = tint,
                locator = locator,
                annotation = annotation
            )
        )
    }

    override suspend fun deleteHighlight(id: Long) {
        bookDao.deleteHighlight(id)
    }

    override suspend fun updateHighlightAnnotation(id: Long, annotation: String) {
        bookDao.updateHighlightAnnotation(id, annotation)
    }

    override suspend fun insertBook(
        url: Url,
        mediaType: MediaType,
        publication: Publication,
        cover: File
    ): Long {
        return bookDao.insertReadiumBook(
            ReadiumBook(
                creation = Date().time,
                title = publication.metadata.title?: url.filename,
                author = publication.metadata.authors.joinToString(","),
                href = url.toString(),
                identifier = publication.metadata.identifier ?: "",
                mediaType = mediaType,
                progression = "{}",
                cover = cover.path
            )
        )
    }

    override suspend fun deleteBook(id: Long) {
       bookDao.deleteReadiumBook(id)
    }
}