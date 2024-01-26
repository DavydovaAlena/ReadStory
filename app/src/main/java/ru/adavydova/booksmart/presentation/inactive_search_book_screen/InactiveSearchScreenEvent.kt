package ru.adavydova.booksmart.presentation.inactive_search_book_screen

import ru.adavydova.booksmart.domain.model.Book
import ru.adavydova.booksmart.presentation.inactive_search_book_screen.filters.FilterBooks
import ru.adavydova.booksmart.presentation.inactive_search_book_screen.filters.LanguageRestrictFilterBooks
import ru.adavydova.booksmart.presentation.inactive_search_book_screen.filters.OrderBooks

sealed class InactiveSearchScreenEvent {

    class OpenOrCloseSearchMenu(val enable: ShowState): InactiveSearchScreenEvent()
    class OpenOrCloseOrderMenu(val enable: ShowState): InactiveSearchScreenEvent()
    class OpenOrCloseFilterMenu(val enable: ShowState): InactiveSearchScreenEvent()
    class OpenOrCloseLanguageMenu(val enable: ShowState): InactiveSearchScreenEvent()


    object CancelAndCloseSearchMenu: InactiveSearchScreenEvent()
    object InsertFilter: InactiveSearchScreenEvent()


    class SelectOrderType (val orderBooks: OrderBooks): InactiveSearchScreenEvent()
    class SelectFilterType (val filterBook: FilterBooks): InactiveSearchScreenEvent()
    class SelectLanguageType (val language: LanguageRestrictFilterBooks): InactiveSearchScreenEvent()


}

