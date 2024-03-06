package ru.adavydova.booksmart.domain.usecase.readium_books

import ru.adavydova.booksmart.domain.model.readium_book.Highlight
import ru.adavydova.booksmart.domain.repository.ReadiumBooksRepository
import javax.inject.Inject

class GetHighlightByIdUseCase @Inject constructor(
    private val repository: ReadiumBooksRepository
) {
    suspend operator fun invoke(highlightId: Long): Highlight?{
        return repository.highlightById(highlightId)
    }
}