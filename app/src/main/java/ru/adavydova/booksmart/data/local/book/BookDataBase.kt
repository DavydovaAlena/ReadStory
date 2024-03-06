package ru.adavydova.booksmart.data.local.book

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.adavydova.booksmart.domain.model.google_book.GoogleBook
import ru.adavydova.booksmart.domain.model.readium_book.Bookmark
import ru.adavydova.booksmart.domain.model.readium_book.Download
import ru.adavydova.booksmart.domain.model.readium_book.Highlight
import ru.adavydova.booksmart.domain.model.readium_book.HighlightConverters
import ru.adavydova.booksmart.domain.model.readium_book.ReadiumBook
import ru.adavydova.booksmart.domain.model.readium_book.TemporaryOpeningBook

@Database(
    entities = [
        GoogleBook::class,
        ReadiumBook::class,
        Bookmark::class,
        Highlight::class,
        Download::class,
        TemporaryOpeningBook::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    HighlightConverters::class,
    Download.Type.Converter::class
)

abstract class BookDataBase : RoomDatabase() {
    abstract val googleBookDao: GoogleBookDao
    abstract val readiumBookDao: ReadiumBooksDao
    abstract val downloadDao: DownloadDao

    companion object {
        const val NAME_DATABASE = "readium_db"
    }
}