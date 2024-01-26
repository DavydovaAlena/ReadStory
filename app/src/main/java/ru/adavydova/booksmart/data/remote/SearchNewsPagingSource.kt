package ru.adavydova.booksmart.data.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import ru.adavydova.booksmart.data.mapper.toBooks
import ru.adavydova.booksmart.domain.model.Book

class SearchNewsPagingSource(
    private val booksApi: BooksApi,
    private val query: String,
    private val maxResults: Int,
    private val filters: Map<String,String>
) : PagingSource<Int, Book>() {
    override fun getRefreshKey(state: PagingState<Int, Book>): Int? {

        return state.anchorPosition?.let {
            val anchorPosition = state.closestPageToPosition(it)
            anchorPosition?.nextKey?.minus(1) ?: anchorPosition?.prevKey?.plus(1)
        }

    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Book> {
        val page = params.key ?: 0

        return try {
            val books = booksApi.searchBooks(
                query = query,
                startIndex = page*maxResults,
                maxResults = maxResults,
                filters = filters
            ).toBooks()


            if (books.books.isEmpty()){
                return LoadResult.Page(emptyList(), prevKey = null, nextKey = null)
            }

            val maxItem = books.totalResult
            val nextPage = if (maxResults * page + 1 < maxItem ) page + 1 else null


            LoadResult.Page(
                data = books.books,
                prevKey = if (page > 0) page - 1 else null,
                nextKey = nextPage
            )

        } catch (e: HttpException) {
            e.printStackTrace()
            return LoadResult.Error(
                throwable = Throwable(e.message)
            )
        }
    }
}