package ru.adavydova.booksmart.data.local.repository

import kotlinx.coroutines.flow.Flow
import ru.adavydova.booksmart.data.local.BookDao
import ru.adavydova.booksmart.domain.model.Book
import ru.adavydova.booksmart.domain.repository.BooksLocalRepository

class BooksLocalRepositoryImpl(
    private val dao: BookDao,
) : BooksLocalRepository {

    override suspend fun insertBook(book: Book) {
        dao.insertBook(book)
    }

    override suspend fun deleteBook(book: Book) {
        dao.deleteBook(book)
    }

    override fun getLocalBooks(): Flow<List<Book>> {
        return dao.getLocalBook()
    }

    override suspend fun getLocalBookById(id: String): Book? {
        return dao.getLocalBookById(id)
    }
}