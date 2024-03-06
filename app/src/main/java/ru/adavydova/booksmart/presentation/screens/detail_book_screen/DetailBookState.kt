package ru.adavydova.booksmart.presentation.screens.detail_book_screen

import ru.adavydova.booksmart.domain.model.google_book.GoogleBook

data class DetailBookState(
    val error: String? = null,
    val favorite: Boolean = false,
    val googleBook: GoogleBook? = null,
    val load: Boolean = false
)