package ru.adavydova.booksmart.data.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.adavydova.booksmart.data.mapper.toBooks
import ru.adavydova.booksmart.domain.model.google_book.GoogleBook

class SearchNewsPagingSource(
    private val booksApi: BooksApi,
    private val query: String,
    private val maxResults: Int,
    private val filters: Map<String,String>
) : PagingSource<Int, GoogleBook>() {
    override fun getRefreshKey(state: PagingState<Int, GoogleBook>): Int? {

        return state.anchorPosition?.let {
            val anchorPosition = state.closestPageToPosition(it)
            anchorPosition?.nextKey?.minus(1) ?: anchorPosition?.prevKey?.plus(1)
        }

    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GoogleBook> {
        val page = params.key ?: 0

        return try {
            val books = booksApi.searchBooks(
                query = query,
                startIndex = if (page == 0) 0 else page * maxResults,
                maxResults = maxResults,
                filters = filters
            ).toBooks()


            if (books.googleBooks.isEmpty()){
                return LoadResult.Page(emptyList(), prevKey = null, nextKey = null)
            }

            if (books.googleBooks.size < maxResults){
                return LoadResult.Page(
                    data = books.googleBooks,
                    prevKey = null,
                    nextKey = null
                )
            }

            Log.d("ok","okkkkkkk")
            LoadResult.Page(
                data = books.googleBooks,
                prevKey = if (page > 0) page - 1 else null,
                nextKey = page + 1
            )

        } catch (e: Exception) {
            return LoadResult.Error(
                throwable = Throwable("error paging data")
            )
        }
    }
}