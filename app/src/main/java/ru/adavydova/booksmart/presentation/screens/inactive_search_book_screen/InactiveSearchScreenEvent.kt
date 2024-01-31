package ru.adavydova.booksmart.presentation.screens.inactive_search_book_screen

import ru.adavydova.booksmart.domain.model.Book
import ru.adavydova.booksmart.presentation.screens.inactive_search_book_screen.filters.FilterBooks
import ru.adavydova.booksmart.presentation.screens.inactive_search_book_screen.filters.FilterTypeBook
import ru.adavydova.booksmart.presentation.screens.inactive_search_book_screen.filters.LanguageRestrictFilterBooks
import ru.adavydova.booksmart.presentation.screens.inactive_search_book_screen.filters.OrderBooks
import kotlin.reflect.KClass

sealed class InactiveSearchScreenEvent {

    class OpenOrCloseSearchMenu(val enable: ShowState): InactiveSearchScreenEvent()
    class OpenOrCloseFilterMenu(val enable: ShowState, val filterType: KClass<out FilterTypeBook>): InactiveSearchScreenEvent()


    object CancelAndCloseSearchMenu: InactiveSearchScreenEvent()
    object InsertFilter: InactiveSearchScreenEvent()

    class SelectFilterType (val filter: String): InactiveSearchScreenEvent()


}

