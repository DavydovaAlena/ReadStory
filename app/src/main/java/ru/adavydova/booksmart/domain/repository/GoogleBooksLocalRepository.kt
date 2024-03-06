package ru.adavydova.booksmart.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.adavydova.booksmart.domain.model.google_book.GoogleBook

interface GoogleBooksLocalRepository {
    suspend fun insertBook(googleBook: GoogleBook)
    suspend fun deleteBook(googleBook: GoogleBook)
    fun getLocalBooks(): Flow<List<GoogleBook>>
    suspend fun getLocalBookById(id: String): GoogleBook?

}