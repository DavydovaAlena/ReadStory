package ru.adavydova.booksmart.presentation.inactive_search_book_screen

import ru.adavydova.booksmart.domain.model.Book
import ru.adavydova.booksmart.presentation.inactive_search_book_screen.filters.FilterBooks
import ru.adavydova.booksmart.presentation.inactive_search_book_screen.filters.OrderBooks

sealed class InactiveSearchScreenEvent {

    object SideBarUse: InactiveSearchScreenEvent()
    object ShowAdditionalParameter: InactiveSearchScreenEvent()
    class AddToFavorites(val book: Book): InactiveSearchScreenEvent()
    class AddToDeferred(val book: Book): InactiveSearchScreenEvent()
    class SelectFilter(val filter: FilterBooks): InactiveSearchScreenEvent()
    class SelectOrderType(val orderType: OrderBooks): InactiveSearchScreenEvent()
    class SelectLanguageBook(val orderType: OrderBooks): InactiveSearchScreenEvent()
    object ApplyAFilter: InactiveSearchScreenEvent()

}

