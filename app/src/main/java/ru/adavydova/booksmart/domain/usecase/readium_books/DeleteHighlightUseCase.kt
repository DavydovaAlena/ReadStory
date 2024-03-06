package ru.adavydova.booksmart.domain.usecase.readium_books

import android.util.Log
import ru.adavydova.booksmart.domain.repository.ReadiumBooksRepository
import ru.adavydova.booksmart.util.Resource
import timber.log.Timber
import javax.inject.Inject

class DeleteHighlightUseCase @Inject constructor(
    private val repository: ReadiumBooksRepository
) {

    suspend operator fun invoke(
        highlightId:Long
    ): Resource<Unit>{
        return try {
            repository.deleteHighlight(highlightId)
            Resource.Success(
                data = Unit)
        } catch (e:Exception){
            Timber.d(e)
            Resource.Error(
                message = "Error delete highlight"
            )
        }
    }
}