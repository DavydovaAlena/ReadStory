package ru.adavydova.booksmart.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.adavydova.booksmart.data.mapper.toBooks
import ru.adavydova.booksmart.domain.model.Book

class SearchNewsPagingSource(
    private val booksApi: BooksApi,
    private val query: String,
) : PagingSource<Int, Book>() {
    override fun getRefreshKey(state: PagingState<Int, Book>): Int? {

        return state.anchorPosition?.let {
            val anchorPosition = state.closestPageToPosition(it)
            anchorPosition?.nextKey?.minus(1) ?: anchorPosition?.prevKey?.plus(1)
        }

    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Book> {
        val page = params.key ?: 1

        return try {
            val booksPaging = booksApi.searchBooks(
                query = query,
                startIndex = page
            )
            val books = booksPaging.toBooks()
            LoadResult.Page(
                data = books.books,
                prevKey = if (page > 1) page - 1 else null,
                nextKey = if (page >= books.books.size / 10) null else page + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(
                throwable = Throwable("error paging data")
            )
        }
    }
}