package ru.adavydova.booksmart.domain.model.readium_book

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.util.Date

@Entity(tableName = TemporaryOpeningBook.TABLE_NAME,)
data class TemporaryOpeningBook (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = ID)
    val id: Long = 0,
    @ColumnInfo(name = COVER, defaultValue = "")
    val cover: String,
    @ColumnInfo(name = PROGRESSION)
    val progression: String,
    @ColumnInfo(name = TITLE, defaultValue = "")
    val title: String,
    @ColumnInfo(name = LAST_OPENING_TIME)
    val lastOpeningTime: Long = 0,
) {

    companion object{
        const val TABLE_NAME = "time_opening"
        const val LAST_OPENING_TIME = "last_opening_time"
        const val ID = "id"
        const val COVER = "cover"
        const val PROGRESSION = "progression"
        const val TITLE = "title"
    }
}
