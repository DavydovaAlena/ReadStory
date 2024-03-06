package ru.adavydova.booksmart.domain.repository

import androidx.annotation.ColorInt
import kotlinx.coroutines.flow.Flow
import org.readium.r2.shared.publication.Locator
import org.readium.r2.shared.publication.Publication
import ru.adavydova.booksmart.domain.model.readium_book.Bookmark
import ru.adavydova.booksmart.domain.model.readium_book.Highlight
import ru.adavydova.booksmart.domain.model.readium_book.ReadiumBook
import org.readium.r2.shared.util.Url
import org.readium.r2.shared.util.mediatype.MediaType
import ru.adavydova.booksmart.domain.model.readium_book.TemporaryOpeningBook
import java.io.File


interface ReadiumBooksRepository {


    suspend fun insertBookOrderByTimeOpening(temporaryOpeningBook: TemporaryOpeningBook)
    suspend fun deleteBookOrderByTimeOpening(bookId: Long)
    fun getBooksOrderByTimeOpening(): Flow<List<TemporaryOpeningBook>>
    suspend fun updateBookOrderByTimeOpening(bookId: Long, progress:String, time:Long)

    fun books(): Flow<List<ReadiumBook>>
    suspend fun get(id:Long): ReadiumBook?
    suspend fun insertBook(
        url: Url,
        mediaType: MediaType,
        publication: Publication,
        cover: File
    ): Long
    suspend fun deleteBook(id: Long)
    suspend fun computeStorageDir(): File
    suspend fun saveProgression(locator: Locator, bookId: Long)

    fun bookmarksForBook(bookId: Long): Flow<List<Bookmark>>
    suspend fun deleteBookmark(bookmarkId: Long)
    suspend fun insertBookmark(
        bookId: Long,
        publication: Publication,
        locator: Locator): Long


    suspend fun deleteHighlight(id: Long)
    suspend fun updateHighlightAnnotation(id: Long, annotation: String)
    suspend fun highlightById(id:Long): Highlight?
    fun highlightsForBook(bookId: Long): Flow<List<Highlight>>
    suspend fun addHighlight(
        bookId:Long,
        style: Highlight.Style,
        @ColorInt tint: Int,
        locator: Locator,
        annotation: String
    ): Long
    suspend fun updateHighlightAnnotationStyle(id: Long, style: Highlight.Style, @ColorInt tint: Int)







}