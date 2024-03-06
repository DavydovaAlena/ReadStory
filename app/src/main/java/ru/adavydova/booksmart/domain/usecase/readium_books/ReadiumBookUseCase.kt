package ru.adavydova.booksmart.domain.usecase.readium_books

import javax.inject.Inject

data class ReadiumBookUseCase @Inject constructor(
    val addHighlightUseCase: AddHighlightUseCase,
    val deleteBookmarkUseCase: DeleteBookmarkUseCase,
    val deleteBookUseCase: DeleteBookUseCase,
    val deleteHighlightUseCase: DeleteHighlightUseCase,
    val getAllBooksUseCase: GetAllBooksUseCase,
    val getBookmarksForBookUseCase: GetBookmarksForBookUseCase,
    val getBookUseCase: GetBookUseCase,
    val getHighlightByIdUseCase: GetHighlightByIdUseCase,
    val getHighlightsForBookUseCase: GetHighlightsForBookUseCase,
    val insertBookUseCase: InsertBookUseCase,
    val insertBookmarkUseCase: InsertBookmarkUseCase,
    val saveProgressionUseCase: SaveProgressionUseCase,
    val updateHighlightAnnotationStyleUseCase: UpdateHighlightAnnotationStyleUseCase,
    val updateHighlightAnnotationUseCase: UpdateHighlightAnnotationUseCase,
    val insertTimeOpeningBookUseCase: InsertTimeOpeningBookUseCase,
    val deleteTimeOpeningBookUseCase: DeleteTimeOpeningBookUseCase,
    val getBooksOrderByTimeOpeningUseCase: GetBooksOrderByTimeOpeningUseCase,
    val updateBookOrderByTimeOpeningUseCase: UpdateBookOrderByTimeOpeningUseCase
)