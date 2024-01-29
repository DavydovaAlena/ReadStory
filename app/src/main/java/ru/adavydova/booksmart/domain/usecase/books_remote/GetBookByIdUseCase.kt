package ru.adavydova.booksmart.domain.usecase.books_remote

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.adavydova.booksmart.data.remote.util.Resource
import ru.adavydova.booksmart.domain.model.Book
import ru.adavydova.booksmart.domain.repository.BooksRemoteRepository

class GetBookByIdUseCase(
    private val repository: BooksRemoteRepository,
) {
    suspend operator fun invoke(bookId: String): Resource<Book> = withContext(Dispatchers.IO){
         repository.getBookById(bookId)
    }
}