package ru.adavydova.booksmart.domain.model

import androidx.room.Entity



data class Books(
    val books: List<Book>,
    val totalResult: Int
)
