package ru.adavydova.booksmart.presentation.screens.detail_book_screen

import ru.adavydova.booksmart.domain.model.Book

data class DetailBookState(
    val error: String? = null,
    val favorite: Boolean = false,
    val book: Book? = null,
    val load: Boolean = false
)