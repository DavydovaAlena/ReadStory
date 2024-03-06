package ru.adavydova.booksmart.domain.usecase.readium_books

import ru.adavydova.booksmart.domain.model.readium_book.TemporaryOpeningBook
import ru.adavydova.booksmart.domain.repository.ReadiumBooksRepository
import ru.adavydova.booksmart.util.Resource
import timber.log.Timber
import javax.inject.Inject

class DeleteTimeOpeningBookUseCase @Inject constructor(
    private val readiumBooksRepository: ReadiumBooksRepository
    ) {
        suspend operator fun invoke(id:Long): Resource<Unit> {
            return try {
                readiumBooksRepository.deleteBookOrderByTimeOpening(id)
                Resource.Success(data = Unit)
            } catch (e:Exception){
                Timber.e(e)
                Resource.Error(
                    message = "Error delete time opening book"
                )
            }
        }
    }