package ru.adavydova.booksmart.presentation.inactive_search_book_screen

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.adavydova.booksmart.domain.model.Book
import ru.adavydova.booksmart.presentation.inactive_search_book_screen.filters.FilterBooks
import ru.adavydova.booksmart.presentation.inactive_search_book_screen.filters.LanguageRestrictFilterBooks
import ru.adavydova.booksmart.presentation.inactive_search_book_screen.filters.OrderBooks

data class InactiveSearchBookScreenState(
    val books: Flow<PagingData<Book>>? = null,
    val query: String = "",
    val orderType: OrderBooks = OrderBooks.RelevanceOrderType,
    val languageRestrict: Set<LanguageRestrictFilterBooks> = setOf(),
    val filter: FilterBooks = FilterBooks.Ebooks,
    val showSideBar: ShowState = ShowState.Close,
    val showAdditionalParameter: ShowState = ShowState.Close,
)

sealed class ShowState{
    object Close: ShowState()
    object Open: ShowState()
}