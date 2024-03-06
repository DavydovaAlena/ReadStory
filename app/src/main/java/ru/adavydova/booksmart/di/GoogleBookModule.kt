package ru.adavydova.booksmart.di


import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.adavydova.booksmart.data.local.book.BookDataBase
import ru.adavydova.booksmart.data.local.repository.GoogleGoogleBooksLocalRepositoryImpl
import ru.adavydova.booksmart.data.remote.BooksApi
import ru.adavydova.booksmart.data.remote.repository.GoogleBooksRemoteRepositoryImpl
import ru.adavydova.booksmart.domain.repository.GoogleBooksLocalRepository
import ru.adavydova.booksmart.domain.repository.GoogleBooksRemoteRepository
import ru.adavydova.booksmart.domain.usecase.google_books_local.BooksLocalUseCase
import ru.adavydova.booksmart.domain.usecase.google_books_local.DeleteLocalBookUseCase
import ru.adavydova.booksmart.domain.usecase.google_books_local.GetLocalBookByIdUseCase
import ru.adavydova.booksmart.domain.usecase.google_books_local.GetLocalBooksUseCase
import ru.adavydova.booksmart.domain.usecase.google_books_local.InsertBookUseCase
import ru.adavydova.booksmart.domain.usecase.google_books_remote.BooksRemoteUseCase
import ru.adavydova.booksmart.domain.usecase.google_books_remote.FilterBooksUseCase
import ru.adavydova.booksmart.domain.usecase.google_books_remote.GetBookByIdUseCase
import ru.adavydova.booksmart.domain.usecase.google_books_remote.SearchBookUseCase
import javax.inject.Singleton



@Module
@InstallIn(SingletonComponent::class)
class GoogleBookModule {

    @Singleton
    @Provides
    fun provideLocalBookRepository(
        bookDataBase: BookDataBase
    ): GoogleBooksLocalRepository {
        return GoogleGoogleBooksLocalRepositoryImpl(
            dao = bookDataBase.googleBookDao
        )
    }

    @Singleton
    @Provides
    fun provideInterceptor(): Interceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }


    @Singleton
    @Provides
    fun provideClient(interceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor).build()
    }


    @Singleton
    @Provides
    fun provideBooksRepository(booksApi: BooksApi): GoogleBooksRemoteRepository {
        return GoogleBooksRemoteRepositoryImpl(booksApi)
    }


    @Singleton
    @Provides
    fun provideBooksApi(client: OkHttpClient): BooksApi {
        return Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/books/v1/").client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BooksApi::class.java)
    }


    @Singleton
    @Provides
    fun provideBooksRemoteUseCase(googleBooksRemoteRepository: GoogleBooksRemoteRepository): BooksRemoteUseCase {
        return BooksRemoteUseCase(
            searchBookUseCase = SearchBookUseCase(googleBooksRemoteRepository),
            filterBooksUseCase = FilterBooksUseCase(googleBooksRemoteRepository),
            getBookByIdUseCase = GetBookByIdUseCase(googleBooksRemoteRepository)
        )
    }

    @Singleton
    @Provides
    fun provideBooksLocalUseCase(googleBooksLocalRepository: GoogleBooksLocalRepository): BooksLocalUseCase {
        return BooksLocalUseCase(
            getLocalBookByIdUseCase = GetLocalBookByIdUseCase(googleBooksLocalRepository),
            insertBookUseCase = InsertBookUseCase(googleBooksLocalRepository),
            deleteLocalBookUseCase = DeleteLocalBookUseCase(googleBooksLocalRepository),
            getLocalBooksUseCase = GetLocalBooksUseCase(googleBooksLocalRepository)
        )
    }
}