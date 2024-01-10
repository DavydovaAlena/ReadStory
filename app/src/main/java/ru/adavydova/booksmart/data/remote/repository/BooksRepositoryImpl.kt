package ru.adavydova.booksmart.data.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.adavydova.booksmart.data.remote.BooksApi
import ru.adavydova.booksmart.data.remote.SearchNewsPagingSource
import ru.adavydova.booksmart.domain.model.Book
import ru.adavydova.booksmart.domain.repository.BooksRepository
import ru.adavydova.booksmart.domain.model.Books

class BooksRepositoryImpl(
    private val api: BooksApi
): BooksRepository {
    override fun searchBooks(
        query: String
        ): Flow<PagingData<Book>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                SearchNewsPagingSource(
                    booksApi = api,
                    query =query,
                )
            }
        ).flow
    }
}