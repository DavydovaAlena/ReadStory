package ru.adavydova.booksmart.domain.usecase.readium_books

import ru.adavydova.booksmart.domain.model.readium_book.TemporaryOpeningBook
import ru.adavydova.booksmart.domain.repository.ReadiumBooksRepository
import ru.adavydova.booksmart.util.Resource
import timber.log.Timber
import javax.inject.Inject

class InsertTimeOpeningBookUseCase @Inject constructor(
    private val readiumBooksRepository: ReadiumBooksRepository
) {
    suspend operator fun invoke(
        id: Long,
        cover: String,
        progression: String,
        title: String?,
        lastOpeningTime: Long
    ): Resource<Unit> {
        return try {
            readiumBooksRepository.insertBookOrderByTimeOpening(
                TemporaryOpeningBook(id, cover, progression, title?:"", lastOpeningTime)
            )
            Resource.Success(data = Unit)
        } catch (e: Exception) {
            Timber.e(e)
            Resource.Error(
                message = "Error insert time opening book"
            )
        }
    }
}