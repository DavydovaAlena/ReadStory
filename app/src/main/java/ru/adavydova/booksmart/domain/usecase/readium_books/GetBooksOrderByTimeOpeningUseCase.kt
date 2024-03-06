package ru.adavydova.booksmart.domain.usecase.readium_books

import kotlinx.coroutines.flow.Flow
import ru.adavydova.booksmart.domain.model.readium_book.ReadiumBook
import ru.adavydova.booksmart.domain.model.readium_book.TemporaryOpeningBook
import ru.adavydova.booksmart.domain.repository.ReadiumBooksRepository
import javax.inject.Inject

class GetBooksOrderByTimeOpeningUseCase @Inject constructor(
    private val readiumBooksRepository: ReadiumBooksRepository
) {
    operator fun invoke(): Flow<List<TemporaryOpeningBook>> {
        return readiumBooksRepository.getBooksOrderByTimeOpening()
    }
}