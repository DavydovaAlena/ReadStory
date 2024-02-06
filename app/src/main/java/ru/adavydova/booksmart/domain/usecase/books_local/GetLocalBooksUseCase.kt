package ru.adavydova.booksmart.domain.usecase.books_local

import kotlinx.coroutines.flow.Flow
import ru.adavydova.booksmart.data.local.repository.BooksLocalRepositoryImpl
import ru.adavydova.booksmart.domain.model.Book
import ru.adavydova.booksmart.domain.repository.BooksLocalRepository
import ru.adavydova.booksmart.util.Resource
import javax.inject.Inject

class GetLocalBooksUseCase(
    private val repository: BooksLocalRepository
)  {
    operator fun invoke (): Resource<Flow<List<Book>>>{
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