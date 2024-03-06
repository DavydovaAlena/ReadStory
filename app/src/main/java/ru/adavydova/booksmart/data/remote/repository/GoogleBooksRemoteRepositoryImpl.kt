package ru.adavydova.booksmart.data.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okio.IOException
import org.json.JSONException
import retrofit2.HttpException
import ru.adavydova.booksmart.data.mapper.toBook
import ru.adavydova.booksmart.data.remote.BooksApi
import ru.adavydova.booksmart.data.remote.SearchNewsPagingSource
import ru.adavydova.booksmart.util.Resource
import ru.adavydova.booksmart.domain.model.google_book.GoogleBook
import ru.adavydova.booksmart.domain.repository.GoogleBooksRemoteRepository

class GoogleBooksRemoteRepositoryImpl(
    private val api: BooksApi
): GoogleBooksRemoteRepository {
    override fun searchBooks(
        query: String,
        maxResult: Int,
        filters: Map<String,String>
        ): Flow<PagingData<GoogleBook>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                SearchNewsPagingSource(
                    booksApi = api,
                    query =query,
                    maxResults = maxResult,
                    filters = filters
                )
            }
        ).flow
    }

    override suspend fun getBookById(bookId: String): Resource<GoogleBook> {
        return try {
            val book = withContext(Dispatchers.IO){
                api.getBookById(bookId).toBook()
            }
            Resource.Success(
                data = book
            )

        }
        catch (e: HttpException){
            e.printStackTrace()
            Resource.Error(
                message = e.message()
            )
        }
        catch (e: IOException){
            e.printStackTrace()
            Resource.Error(
                message = e.message?: "no info"
            )
        } catch (e:JSONException){
            e.printStackTrace()
            Resource.Error(
                message = e.message?: "no info"
            )
        }
    }
}