package ru.adavydova.booksmart.data.local.book

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.adavydova.booksmart.domain.model.Book


@Database(
    entities = [Book::class],
    version = 1
)

abstract class BookFavoriteDataBase: RoomDatabase() {
    abstract val dao: BookDao

    companion object{
       const val NAME_DB = "book_db"
    }
}