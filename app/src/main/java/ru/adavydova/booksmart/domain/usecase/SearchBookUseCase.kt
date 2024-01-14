package ru.adavydova.booksmart.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.adavydova.booksmart.domain.model.Book
import ru.adavydova.booksmart.domain.repository.BooksRepository

class SearchBookUseCase (
    private val repository: BooksRepository
){
    operator fun invoke(query: String, maxResults: Int): Flow<PagingData<Book>>{
        return repository.searchBooks(query = query, maxResult = maxResults)
    }
}