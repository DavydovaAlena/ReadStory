package ru.adavydova.booksmart.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.adavydova.booksmart.domain.model.Book


@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book)
    @Delete
    suspend fun deleteBook(book: Book)

    @Query("SELECT * FROM book_table")
    fun getLocalBook(): Flow<List<Book>>

    @Query("SELECT * FROM book_table WHERE id =:id")
    suspend fun getLocalBookById(id:String): Book
}