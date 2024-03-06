package ru.adavydova.booksmart.domain.usecase.readium_books

import org.readium.r2.shared.publication.Locator
import org.readium.r2.shared.publication.Publication
import ru.adavydova.booksmart.domain.repository.ReadiumBooksRepository
import ru.adavydova.booksmart.util.Resource
import timber.log.Timber
import javax.inject.Inject


class InsertBookmarkUseCase @Inject constructor(
    private val repository: ReadiumBooksRepository
) {

    suspend operator fun invoke(
        bookId: Long,
        publication: Publication,
        locator: Locator
    ): Resource<Long> {
        return try {
            Resource.Success(
                data = repository.insertBookmark(
                    bookId, publication, locator
                )
            )
        } catch (e: Exception) {
            Timber.e(e)
            Resource.Error("Error adding a bookmark")
        }
    }
}