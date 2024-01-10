package ru.adavydova.booksmart.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.adavydova.booksmart.domain.model.Book
import ru.adavydova.booksmart.domain.model.Books

interface BooksRepository {
     fun searchBooks(
        query: String): Flow<PagingData<Book>>
}