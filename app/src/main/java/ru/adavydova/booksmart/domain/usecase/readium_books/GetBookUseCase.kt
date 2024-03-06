package ru.adavydova.booksmart.domain.usecase.readium_books

import ru.adavydova.booksmart.domain.model.readium_book.ReadiumBook
import ru.adavydova.booksmart.domain.repository.ReadiumBooksRepository
import javax.inject.Inject


class GetBookUseCase @Inject constructor(
    private val repository: ReadiumBooksRepository
) {
    suspend operator fun invoke(bookId:Long): ReadiumBook?{
        return repository.get(bookId)
    }
}