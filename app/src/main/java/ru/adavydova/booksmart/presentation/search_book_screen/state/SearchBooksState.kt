package ru.adavydova.booksmart.presentation.search_book_screen.state

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.adavydova.booksmart.domain.model.Book
import ru.adavydova.booksmart.presentation.util.ResultState

data class SearchBooksState(
    val books: Flow<PagingData<Book>>? = null,
    val query: String = "",
)