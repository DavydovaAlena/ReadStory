package ru.adavydova.booksmart.domain.usecase.readium_books

import androidx.annotation.ColorInt
import ru.adavydova.booksmart.domain.model.readium_book.Highlight
import ru.adavydova.booksmart.domain.repository.ReadiumBooksRepository
import javax.inject.Inject

class UpdateHighlightAnnotationStyleUseCase @Inject constructor(
    private val repository: ReadiumBooksRepository
) {

    suspend operator fun invoke(
        highlightId:Long,
        style:Highlight.Style,
        @ColorInt tint:Int){
        repository.updateHighlightAnnotationStyle(highlightId, style, tint)
    }
}