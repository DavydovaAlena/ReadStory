package ru.adavydova.booksmart.domain.model

data class Books(
    val totalResult: Int,
    val books: List<Book>
)