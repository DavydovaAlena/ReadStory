package ru.adavydova.booksmart.presentation.detail_book

import ru.adavydova.booksmart.domain.model.Book

sealed class DetailBookEvent {
    object DeleteOrInsertBook: DetailBookEvent()
    object GetLocalBookById: DetailBookEvent()
}