package ru.adavydova.booksmart.domain.usecase.books_local

import ru.adavydova.booksmart.data.local.repository.BooksLocalRepositoryImpl
import ru.adavydova.booksmart.domain.model.Book
import ru.adavydova.booksmart.domain.repository.BooksLocalRepository
import javax.inject.Inject

class InsertBookUseCase(
    private val repository: BooksLocalRepository
)  {
    suspend operator fun invoke(book: Book){
        repository.insertBook(book)
    }
}