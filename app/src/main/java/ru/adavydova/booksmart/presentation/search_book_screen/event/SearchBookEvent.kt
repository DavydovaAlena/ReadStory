package ru.adavydova.booksmart.presentation.search_book_screen.event

sealed class SearchBookEvent {
    class UpdateAndSearchQuery(val query: String): SearchBookEvent()

}