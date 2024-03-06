package ru.adavydova.booksmart.presentation.screens.inactive_search_book_screen

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.adavydova.booksmart.domain.model.google_book.GoogleBook
import ru.adavydova.booksmart.presentation.screens.inactive_search_book_screen.filters.FilterBooks
import ru.adavydova.booksmart.presentation.screens.inactive_search_book_screen.filters.LanguageRestrictFilterBooks
import ru.adavydova.booksmart.presentation.screens.inactive_search_book_screen.filters.OrderBooks

data class InactiveSearchBookScreenState(
    val books: Flow<PagingData<GoogleBook>>? = null,
    val query: String = "",
    val orderType: OrderBooks = OrderBooks.RelevanceOrderType,
    val languageRestrict: LanguageRestrictFilterBooks = LanguageRestrictFilterBooks.AllLanguage,
    val filter: FilterBooks = FilterBooks.Ebooks,
    val showSearchMenu: ShowState = ShowState.Close,
    val showOrderMenu: ShowState = ShowState.Close,
    val showFilterMenu: ShowState = ShowState.Close,
    val showLanguageMenu: ShowState = ShowState.Close,

    )

sealed class ShowState{
    object Close: ShowState()
    object Open: ShowState()

 operator fun invoke(): Boolean{
     return when(this){
         Close -> false
         Open -> true
     }
 }

}
fun Boolean.getShowState(): ShowState {
    return when(this){
        true -> ShowState.Open
        false -> ShowState.Close
    }
}
