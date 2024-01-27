package ru.adavydova.booksmart.domain.model

import androidx.room.Entity


@Entity
data class AccessInfo(
    val epub: BookFormat,
    val pdf: BookFormat,
    val webReaderLink: String,
)
