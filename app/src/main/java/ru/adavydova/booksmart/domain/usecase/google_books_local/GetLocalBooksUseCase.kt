package ru.adavydova.booksmart.domain.usecase.google_books_local

import kotlinx.coroutines.flow.Flow
import ru.adavydova.booksmart.domain.model.google_book.GoogleBook
import ru.adavydova.booksmart.domain.repository.GoogleBooksLocalRepository
import ru.adavydova.booksmart.util.Resource

class GetLocalBooksUseCase(
    private val repository: GoogleBooksLocalRepository
)  {
    operator fun invoke (): Resource<Flow<List<GoogleBook>>>{
        return try {
            val books = repository.getLocalBooks()
            Resource.Success(
                data = books
            )
        }catch (e:Exception){
            Resource.Error(
                message = e.message?:"Not found"
            )
        }
    }
}