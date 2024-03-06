package ru.adavydova.booksmart.domain.usecase.google_books_local

import ru.adavydova.booksmart.domain.model.google_book.GoogleBook
import ru.adavydova.booksmart.domain.repository.GoogleBooksLocalRepository

class InsertBookUseCase(
    private val repository: GoogleBooksLocalRepository
)  {
    suspend operator fun invoke(googleBook: GoogleBook){
        repository.insertBook(googleBook)
    }
}