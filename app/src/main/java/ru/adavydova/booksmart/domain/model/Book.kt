package ru.adavydova.booksmart.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "book_table")
data class Book(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    @ColumnInfo(defaultValue = "")
    val title: String,
    val authors: String,
    val publisher: String?,
    val publishedDate: String?,
    val description: String?,
    val pageCount: Int?,
    val identifier: String?,
    val categories: String,
    val imageLinks: String?,
    val language: String,
    val saleability: String?,
    val amount: Double?,
    val currencyCode: String?,
    val buyLink: String?,
    val webReaderLink: String?,
    val isAvailablePdf: Boolean?,
    val acsTokenLinkPdf:String?,
    val downloadLinkPdf: String?,
    val isAvailableEpub: Boolean?,
    val acsTokenLinkEpub:String?,
    val downloadLinkEpub: String?,
)