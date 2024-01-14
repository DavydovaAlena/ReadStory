package ru.adavydova.booksmart.presentation.inactive_search_book_screen.filters

sealed class OrderBooks(val order: String) {
    object NewestOrderType: OrderBooks("newest")
    object RelevanceOrderType: OrderBooks("relevance")

}