package ru.adavydova.booksmart.domain.usecase.readium_books

import org.readium.r2.shared.publication.Locator
import ru.adavydova.booksmart.domain.repository.ReadiumBooksRepository
import javax.inject.Inject


class SaveProgressionUseCase @Inject constructor(
    private val repository: ReadiumBooksRepository
){
    suspend operator fun invoke(locator: Locator, bookId:Long){
        repository.saveProgression(locator, bookId)
    }
}