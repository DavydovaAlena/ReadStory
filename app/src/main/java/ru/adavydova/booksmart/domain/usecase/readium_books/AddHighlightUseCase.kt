package ru.adavydova.booksmart.domain.usecase.readium_books

import androidx.annotation.ColorInt
import org.readium.r2.shared.publication.Locator
import ru.adavydova.booksmart.domain.model.readium_book.Highlight
import ru.adavydova.booksmart.domain.repository.ReadiumBooksRepository
import javax.inject.Inject

class AddHighlightUseCase @Inject constructor(
    private val repository: ReadiumBooksRepository
) {

    suspend operator fun invoke(
        bookId:Long,
        style: Highlight.Style,
        @ColorInt tint: Int,
        locator: Locator,
        annotation:String
    ): Long{
        return repository.addHighlight(
            bookId = bookId,
            style = style,
            tint = tint,
            locator = locator,
            annotation = annotation
        )
    }
}