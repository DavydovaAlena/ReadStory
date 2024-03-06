package ru.adavydova.booksmart.data.local.book

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.adavydova.booksmart.domain.model.readium_book.Download

@Dao
interface DownloadDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(download: Download)

    @Query(
        "DELETE FROM " + Download.TABLE_NAME +
                " WHERE " + Download.ID + " = :id AND " + Download.TYPE + " = :type"
    )
    suspend fun delete(id: String, type: Download.Type)

    @Query(
        "SELECT * FROM " + Download.TABLE_NAME +
                " WHERE " + Download.ID + " = :id AND " + Download.TYPE + " = :type"
    )
    suspend fun get(id: String, type: Download.Type): Download?

    @Query(
        "SELECT * FROM " + Download.TABLE_NAME +
                " WHERE " + Download.TYPE + " = :type"
    )
    suspend fun getDownloads(type: Download.Type): List<Download>
}