package ru.adavydova.booksmart.domain.usecase.google_books_local

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.adavydova.booksmart.domain.model.google_book.GoogleBook
import ru.adavydova.booksmart.domain.repository.GoogleBooksLocalRepository

class GetLocalBookByIdUseCase(
    private val repository: GoogleBooksLocalRepository) {
    suspend operator fun invoke(id: String): GoogleBook? = withContext(Dispatchers.IO) {
        repository.getLocalBookById(id)
    }

}