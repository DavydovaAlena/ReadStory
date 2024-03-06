package ru.adavydova.booksmart.domain.usecase.readium_books

import ru.adavydova.booksmart.domain.repository.ReadiumBooksRepository
import javax.inject.Inject

class DeleteBookUseCase @Inject constructor(
    private val repository: ReadiumBooksRepository
) {
    suspend operator fun invoke(bookId:Long){
        repository.deleteBook(bookId)

    }
}