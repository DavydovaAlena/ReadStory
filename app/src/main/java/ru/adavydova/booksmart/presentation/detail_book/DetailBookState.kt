package ru.adavydova.booksmart.presentation.detail_book

import ru.adavydova.booksmart.domain.model.Book

data class DetailBookState(
    val error: String? = null,
    val book: Book? = null,
    val load: Boolean = false
)