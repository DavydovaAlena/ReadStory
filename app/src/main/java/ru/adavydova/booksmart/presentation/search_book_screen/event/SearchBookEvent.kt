package ru.adavydova.booksmart.presentation.search_book_screen.event

sealed class SearchBookEvent {
    class UpdateQuery(val query: String): SearchBookEvent()
    object SearchNews: SearchBookEvent()
    class GoogleAssistantUse(val query: String): SearchBookEvent()

}