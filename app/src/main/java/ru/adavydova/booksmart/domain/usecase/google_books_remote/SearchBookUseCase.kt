package ru.adavydova.booksmart.domain.usecase.google_books_remote

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.adavydova.booksmart.domain.model.google_book.GoogleBook
import ru.adavydova.booksmart.domain.repository.GoogleBooksRemoteRepository

class SearchBookUseCase (
    private val repository: GoogleBooksRemoteRepository
){
    operator fun invoke(query: String, maxResults: Int): Flow<PagingData<GoogleBook>>{
        return repository.searchBooks(query = query, maxResult = maxResults)
    }
}