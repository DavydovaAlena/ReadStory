package ru.adavydova.booksmart.domain.usecase.readium_books

import kotlinx.coroutines.flow.Flow
import ru.adavydova.booksmart.domain.model.readium_book.Highlight
import ru.adavydova.booksmart.domain.repository.ReadiumBooksRepository
import javax.inject.Inject

class GetHighlightsForBookUseCase @Inject constructor(
    private val repository: ReadiumBooksRepository
) {

     operator fun invoke(bookId:Long): Flow<List<Highlight>> {
        return repository.highlightsForBook(bookId)
     }
}