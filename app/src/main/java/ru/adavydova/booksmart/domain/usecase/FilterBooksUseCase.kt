package ru.adavydova.booksmart.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.adavydova.booksmart.domain.model.Book
import ru.adavydova.booksmart.domain.repository.BooksRepository

class FilterBooksUseCase(
    val repository: BooksRepository
) {
    operator fun invoke(query: String, filters: HashMap<String,String>): Flow<PagingData<Book>>{
        return repository.searchBooks(
            query = query,
            maxResult = 10,
            filters = filters
        )
    }
}


