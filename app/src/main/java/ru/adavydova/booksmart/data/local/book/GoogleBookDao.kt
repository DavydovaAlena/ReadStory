package ru.adavydova.booksmart.data.local.book

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.adavydova.booksmart.domain.model.google_book.GoogleBook


@Dao
interface GoogleBookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(googleBook: GoogleBook)
    @Delete
    suspend fun deleteBook(googleBook: GoogleBook)

    @Query("SELECT * FROM book_table")
    fun getLocalBook(): Flow<List<GoogleBook>>

    @Query("SELECT * FROM book_table WHERE id =:id")
    suspend fun getLocalBookById(id:String): GoogleBook
}