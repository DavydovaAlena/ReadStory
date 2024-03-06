package ru.adavydova.booksmart.domain.usecase.readium_books

import kotlinx.coroutines.flow.Flow
import ru.adavydova.booksmart.domain.model.readium_book.ReadiumBook
import ru.adavydova.booksmart.domain.repository.ReadiumBooksRepository
import ru.adavydova.booksmart.util.Resource
import java.io.IOException
import javax.inject.Inject

class GetAllBooksUseCase @Inject constructor(
    private val repository: ReadiumBooksRepository
) {
    operator fun invoke(): Resource<Flow<List<ReadiumBook>>> {
        return try {
            Resource.Success(
                data = repository.books()
            )
        } catch (e: IOException) {
            Resource.Error("Error retrieving books from the database")
        }
    }
}