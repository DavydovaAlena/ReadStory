package ru.adavydova.booksmart.domain.usecase.books_local

import kotlinx.coroutines.flow.Flow
import ru.adavydova.booksmart.data.local.repository.BooksLocalRepositoryImpl
import ru.adavydova.booksmart.domain.model.Book
import ru.adavydova.booksmart.domain.repository.BooksLocalRepository
import javax.inject.Inject

class GetLocalBooksUseCase(
    private val repository: BooksLocalRepository
)  {
    operator fun invoke (): Flow<List<Book>>{
        return repository.getLocalBooks()
    }
}