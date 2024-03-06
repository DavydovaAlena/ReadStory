package ru.adavydova.booksmart.domain.usecase.readium_books

import ru.adavydova.booksmart.domain.repository.ReadiumBooksRepository
import javax.inject.Inject

class UpdateHighlightAnnotationUseCase @Inject constructor(
    private val repository: ReadiumBooksRepository
) {
    suspend operator fun invoke(
        highlightId:Long,
        annotation: String
    ){
        repository.updateHighlightAnnotation(highlightId, annotation)
    }
}