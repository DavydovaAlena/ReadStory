package ru.adavydova.booksmart.domain.usecase.books_local

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.adavydova.booksmart.data.local.repository.BooksLocalRepositoryImpl
import ru.adavydova.booksmart.domain.model.Book
import ru.adavydova.booksmart.domain.repository.BooksLocalRepository
import javax.inject.Inject

class GetLocalBookByIdUseCase(
    private val repository: BooksLocalRepository) {
    suspend operator fun invoke(id: String): Book? = withContext(Dispatchers.IO) {
        repository.getLocalBookById(id)
    }

}