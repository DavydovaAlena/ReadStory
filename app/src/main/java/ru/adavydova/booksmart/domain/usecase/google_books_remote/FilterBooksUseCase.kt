package ru.adavydova.booksmart.domain.usecase.google_books_remote

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.adavydova.booksmart.domain.model.google_book.GoogleBook
import ru.adavydova.booksmart.domain.repository.GoogleBooksRemoteRepository
import ru.adavydova.booksmart.presentation.screens.inactive_search_book_screen.filters.FilterBooks
import ru.adavydova.booksmart.presentation.screens.inactive_search_book_screen.filters.LanguageRestrictFilterBooks
import ru.adavydova.booksmart.presentation.screens.inactive_search_book_screen.filters.OrderBooks

class FilterBooksUseCase(
    val repository: GoogleBooksRemoteRepository
) {
    operator fun invoke(
        query: String,
        maxResult: Int,
        orderType: OrderBooks,
        filter: FilterBooks,
        languageRestrict: LanguageRestrictFilterBooks
    ): Flow<PagingData<GoogleBook>> {

       val filters =  when(languageRestrict){
            LanguageRestrictFilterBooks.AllLanguage -> {
                hashMapOf(
                    "orderBy" to orderType.order,
                    "filter" to filter.filter,
                )
            }
            else -> {
                hashMapOf(
                    "orderBy" to orderType.order,
                    "filter" to filter.filter,
                    "langRestrict" to languageRestrict.language
                )
            }
        }

        return repository.searchBooks(
            query = query,
            maxResult = maxResult,
            filters = filters
        )
    }
}


