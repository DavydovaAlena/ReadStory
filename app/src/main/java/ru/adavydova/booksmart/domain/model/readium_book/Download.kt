package ru.adavydova.booksmart.domain.model.readium_book

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter

@Entity(tableName = Download.TABLE_NAME, primaryKeys = [Download.ID, Download.TYPE])
data class Download(
    @ColumnInfo(name = TYPE)
    val type: Type,
    @ColumnInfo(name = ID)
    val id: String,
    @ColumnInfo(name = COVER)
    val cover: String? = null,
    @ColumnInfo(name = CREATION_DATE, defaultValue = "CURRENT_TIMESTAMP")
    val creation: Long? = null
){
    enum class Type(val value:String){
        OPDS("opds"), LSP("lcp");

        @ProvidedTypeConverter
        class Converter{
            private val values = entries.associateBy(Type::value)

            @TypeConverter
            fun fromString(value: String?): Type = values[value]!!

            @TypeConverter
            fun toString(type:Type): String = type.value
        }

    }
    companion object {
        const val TABLE_NAME = "downloads"
        const val CREATION_DATE = "creation_date"
        const val ID = "id"
        const val TYPE = "type"
        const val COVER = "cover"
    }


}