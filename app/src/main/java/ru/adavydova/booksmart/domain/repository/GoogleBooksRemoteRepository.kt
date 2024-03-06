package ru.adavydova.booksmart.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.adavydova.booksmart.util.Resource
import ru.adavydova.booksmart.domain.model.google_book.GoogleBook

interface GoogleBooksRemoteRepository {
     fun searchBooks(
          query: String,
          maxResult: Int,
          filters: Map<String,String> = hashMapOf()
     ): Flow<PagingData<GoogleBook>>

     suspend fun getBookById(bookId: String): Resource<GoogleBook>

}