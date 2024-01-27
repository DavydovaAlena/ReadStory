package ru.adavydova.booksmart.domain.model

import androidx.room.Entity


@Entity
data class Books(
    val books: List<Book>,
    val totalResult: Int
)
