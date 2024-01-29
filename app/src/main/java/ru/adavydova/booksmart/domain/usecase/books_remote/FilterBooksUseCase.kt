package ru.adavydova.booksmart.domain.usecase.books_remote

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.adavydova.booksmart.domain.model.Book
import ru.adavydova.booksmart.domain.repository.BooksRemoteRepository
import ru.adavydova.booksmart.presentation.inactive_search_book_screen.filters.FilterBooks
import ru.adavydova.booksmart.presentation.inactive_search_book_screen.filters.LanguageRestrictFilterBooks
import ru.adavydova.booksmart.presentation.inactive_search_book_screen.filters.OrderBooks

class FilterBooksUseCase(
    val repository: BooksRemoteRepository
) {
    operator fun invoke(
        query: String,
        maxResult: Int,
        orderType: OrderBooks,
        filter: FilterBooks,
        languageRestrict: LanguageRestrictFilterBooks
    ): Flow<PagingData<Book>> {

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


