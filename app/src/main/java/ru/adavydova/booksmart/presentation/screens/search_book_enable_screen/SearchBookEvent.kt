package ru.adavydova.booksmart.presentation.screens.search_book_enable_screen

sealed class SearchBookEvent {
    class UpdateAndSearchQuery(val query: String): SearchBookEvent()
    object ClearQuery: SearchBookEvent()

}