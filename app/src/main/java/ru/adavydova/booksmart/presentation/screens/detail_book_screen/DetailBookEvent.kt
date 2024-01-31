package ru.adavydova.booksmart.presentation.screens.detail_book_screen

sealed class DetailBookEvent {
    object DeleteOrInsertBook: DetailBookEvent()
    object GetLocalBookById: DetailBookEvent()
}