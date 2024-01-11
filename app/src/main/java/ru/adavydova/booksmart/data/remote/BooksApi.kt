package ru.adavydova.booksmart.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import ru.adavydova.booksmart.data.remote.dto.books.BooksDto


interface BooksApi {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("startIndex")startIndex: Int,

    ): BooksDto


}
