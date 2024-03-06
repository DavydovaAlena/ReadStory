package ru.adavydova.booksmart.domain.usecase.readium_books

import kotlinx.coroutines.flow.Flow
import ru.adavydova.booksmart.domain.model.readium_book.Bookmark
import ru.adavydova.booksmart.domain.repository.ReadiumBooksRepository
import ru.adavydova.booksmart.util.Resource
import timber.log.Timber
import javax.inject.Inject

class GetBookmarksForBookUseCase @Inject constructor(
    private val repository: ReadiumBooksRepository) {

    operator fun invoke(bookId:Long): Resource<Flow<List<Bookmark>>>{
        return try {
          Resource.Success(
              data = repository.bookmarksForBook(bookId)
          )
        }catch (e:Exception){
            Timber.e(e.message)
            Resource.Error("Error receiving bookmarks")
        }
    }
}