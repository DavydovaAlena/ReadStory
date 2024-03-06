package ru.adavydova.booksmart.domain.usecase.readium_books

import org.readium.r2.shared.publication.Publication
import org.readium.r2.shared.util.Url
import org.readium.r2.shared.util.http.HttpRequest
import org.readium.r2.shared.util.mediatype.MediaType
import ru.adavydova.booksmart.domain.repository.ReadiumBooksRepository
import java.io.File
import javax.inject.Inject

class InsertBookUseCase @Inject constructor(
    private val repository: ReadiumBooksRepository
) {
    suspend operator fun invoke(
        url: Url,
        mediaType: MediaType,
        publication: Publication,
        cover:File
    ): Long{
        return repository.insertBook(
            url = url,
            mediaType = mediaType,
            publication = publication,
            cover = cover
        )
    }
}