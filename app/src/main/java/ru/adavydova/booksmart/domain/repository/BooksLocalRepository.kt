package ru.adavydova.booksmart.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.adavydova.booksmart.domain.model.Book

interface BooksLocalRepository {
    suspend fun insertBook(book: Book)
    suspend fun deleteBook(book: Book)
    fun getLocalBooks(): Flow<List<Book>>
    suspend fun getLocalBookById(id: String): Book?
}