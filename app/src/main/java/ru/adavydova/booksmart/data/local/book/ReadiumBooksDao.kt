package ru.adavydova.booksmart.data.local.book

import androidx.annotation.ColorInt
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.adavydova.booksmart.domain.model.readium_book.Bookmark
import ru.adavydova.booksmart.domain.model.readium_book.Highlight
import ru.adavydova.booksmart.domain.model.readium_book.ReadiumBook
import ru.adavydova.booksmart.domain.model.readium_book.TemporaryOpeningBook
import ru.adavydova.booksmart.util.Resource

@Dao
interface ReadiumBooksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReadiumBook(book:ReadiumBook): Long

    @Query("DELETE FROM " + ReadiumBook.TABLE_NAME + " WHERE " + ReadiumBook.ID + " = :bookId")
    suspend fun deleteReadiumBook(bookId:Long)

    @Query("SELECT * FROM " + ReadiumBook.TABLE_NAME + " WHERE " + ReadiumBook.ID + " = :id")
    suspend fun get(id: Long): ReadiumBook?

    @Query("SELECT * FROM " + ReadiumBook.TABLE_NAME + " ORDER BY " + ReadiumBook.CREATION_DATE + " desc")
    fun getAllBooks(): Flow<List<ReadiumBook>>

    @Query("SELECT * FROM " + Bookmark.TABLE_NAME + " WHERE " + Bookmark.BOOK_ID + " = :bookId")
    fun getBookmarksForBook(bookId: Long): Flow<List<Bookmark>>



    @Query("SELECT * FROM ${Highlight.TABLE_NAME} WHERE ${Highlight.BOOK_ID} =:bookId ORDER BY ${Highlight.TOTAL_PROGRESSION} ASC")
    fun getHighlightsForBook(bookId: Long): Flow<List<Highlight>>

    @Query("SELECT * FROM ${Highlight.TABLE_NAME} WHERE ${Highlight.ID} = :highlightId")
    suspend fun getHighlightById(highlightId: Long): Highlight?


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBookmark(bookmark: Bookmark): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHighlight(highlight: Highlight): Long

    @Query("UPDATE ${TemporaryOpeningBook.TABLE_NAME} SET ${TemporaryOpeningBook.PROGRESSION} =:progress, ${TemporaryOpeningBook.LAST_OPENING_TIME} =:time  WHERE  ${TemporaryOpeningBook.ID} =:bookId ")
    suspend fun updateTimeOpeningBook(progress: String, time: Long, bookId:Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeOpeningBook(temporaryOpeningBook: TemporaryOpeningBook)

    @Query("DELETE FROM ${TemporaryOpeningBook.TABLE_NAME} WHERE ${TemporaryOpeningBook.ID} =:id")
    suspend fun deleteTimeOpeningBook(id: Long)

    @Query("SELECT * FROM ${TemporaryOpeningBook.TABLE_NAME} ORDER BY ${TemporaryOpeningBook.LAST_OPENING_TIME} DESC")
    fun getReadiumBooksSortedByOrderingTime():Flow<List<TemporaryOpeningBook>>

    @Query(
        "UPDATE ${Highlight.TABLE_NAME} SET ${Highlight.ANNOTATION} = :annotation WHERE ${Highlight.ID} = :id"
    )
    suspend fun updateHighlightAnnotation(id: Long, annotation: String)


    @Query(
        "UPDATE ${Highlight.TABLE_NAME} SET ${Highlight.TINT} = :tint, ${Highlight.STYLE} = :style WHERE ${Highlight.ID} = :id"
    )
    suspend fun updateHighlightStyle(id: Long, style: Highlight.Style, @ColorInt tint: Int)

    @Query("DELETE FROM " + Bookmark.TABLE_NAME + " WHERE " + Bookmark.ID + " = :id")
    suspend fun deleteBookmark(id: Long)

    @Query("DELETE FROM ${Highlight.TABLE_NAME} WHERE ${Highlight.ID} = :id")
    suspend fun deleteHighlight(id: Long)


    @Query(
        "UPDATE " + ReadiumBook.TABLE_NAME + " SET " + ReadiumBook.PROGRESSION + " = :locator WHERE " + ReadiumBook.ID + "= :id"
    )
    suspend fun saveProgression(locator: String, id: Long)

}
