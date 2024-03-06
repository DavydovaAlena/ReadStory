package ru.adavydova.booksmart.data.local.repository

import kotlinx.coroutines.flow.Flow
import ru.adavydova.booksmart.data.local.book.GoogleBookDao
import ru.adavydova.booksmart.domain.model.google_book.GoogleBook
import ru.adavydova.booksmart.domain.repository.GoogleBooksLocalRepository

class GoogleGoogleBooksLocalRepositoryImpl(
    private val dao: GoogleBookDao,
) : GoogleBooksLocalRepository {

    override suspend fun insertBook(googleBook: GoogleBook) {
        dao.insertBook(googleBook)
    }

    override suspend fun deleteBook(googleBook: GoogleBook) {
        dao.deleteBook(googleBook)
    }

    override fun getLocalBooks(): Flow<List<GoogleBook>> {
        return dao.getLocalBook()
    }

    override suspend fun getLocalBookById(id: String): GoogleBook? {
        return dao.getLocalBookById(id)
    }


}