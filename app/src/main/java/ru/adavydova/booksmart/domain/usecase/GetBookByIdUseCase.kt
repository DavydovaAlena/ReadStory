package ru.adavydova.booksmart.domain.usecase

import ru.adavydova.booksmart.data.remote.util.Resource
import ru.adavydova.booksmart.domain.model.Book
import ru.adavydova.booksmart.domain.repository.BooksRepository

class GetBookByIdUseCase(
    val repository: BooksRepository
) {
    suspend operator fun invoke(bookId: String): Resource<Book>{
        return repository.getBookById(bookId)
    }
}