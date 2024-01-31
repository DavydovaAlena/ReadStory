package ru.adavydova.booksmart.presentation.screens.search_book_enable_screen

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.adavydova.booksmart.domain.model.Book

data class SearchBooksState(
    val books: Flow<PagingData<Book>>? = null,
    val query: String = "",
)