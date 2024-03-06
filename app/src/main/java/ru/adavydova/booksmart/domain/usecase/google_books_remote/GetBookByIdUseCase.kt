package ru.adavydova.booksmart.domain.usecase.google_books_remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.adavydova.booksmart.util.Resource
import ru.adavydova.booksmart.domain.model.google_book.GoogleBook
import ru.adavydova.booksmart.domain.repository.GoogleBooksRemoteRepository

class GetBookByIdUseCase(
    private val repository: GoogleBooksRemoteRepository,
) {
    suspend operator fun invoke(bookId: String): Resource<GoogleBook> = withContext(Dispatchers.IO){
         repository.getBookById(bookId)
    }
}