package ru.adavydova.booksmart.domain.model

import androidx.room.Entity


@Entity
data class BookFormat(
    val isAvailable: Boolean?,
    val acsTokenLink:String?,
    val downloadLink: String?
)