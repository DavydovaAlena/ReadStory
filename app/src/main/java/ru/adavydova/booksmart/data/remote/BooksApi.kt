package ru.adavydova.booksmart.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import ru.adavydova.booksmart.data.remote.dto.books.BooksDto
import ru.adavydova.booksmart.data.remote.dto.books.ItemDto


interface BooksApi {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("startIndex")startIndex: Int,
        @Query("maxResults") maxResults: Int,
        @QueryMap filters: Map<String, String>

    ): BooksDto

    @GET("volumes/{volumeId}")
    suspend fun getBookById(
        @Path("volumeId") bookId: String
    ) : ItemDto

}
