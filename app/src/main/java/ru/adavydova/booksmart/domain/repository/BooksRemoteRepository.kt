package ru.adavydova.booksmart.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.adavydova.booksmart.util.Resource
import ru.adavydova.booksmart.domain.model.Book

interface BooksRemoteRepository {
     fun searchBooks(
          query: String,
          maxResult: Int,
          filters: Map<String,String> = hashMapOf()
     ): Flow<PagingData<Book>>

     suspend fun getBookById(bookId: String): Resource<Book>

}