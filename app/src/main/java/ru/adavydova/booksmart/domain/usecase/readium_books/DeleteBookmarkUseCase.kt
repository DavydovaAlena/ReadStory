package ru.adavydova.booksmart.domain.usecase.readium_books

import ru.adavydova.booksmart.domain.repository.ReadiumBooksRepository
import javax.inject.Inject


class DeleteBookmarkUseCase @Inject constructor(
    private val repository: ReadiumBooksRepository
) {
    suspend operator fun invoke(bookmarkId:Long){
        repository.deleteBookmark(bookmarkId)
    }
}