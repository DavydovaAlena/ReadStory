package ru.adavydova.booksmart.di


import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.adavydova.booksmart.data.local.book.BookFavoriteDataBase
import ru.adavydova.booksmart.data.local.repository.BooksLocalRepositoryImpl
import ru.adavydova.booksmart.data.remote.BooksApi
import ru.adavydova.booksmart.data.remote.repository.BooksRemoteRepositoryImpl
import ru.adavydova.booksmart.domain.repository.BooksLocalRepository
import ru.adavydova.booksmart.domain.repository.BooksRemoteRepository
import ru.adavydova.booksmart.domain.usecase.books_local.BooksLocalUseCase
import ru.adavydova.booksmart.domain.usecase.books_local.DeleteLocalBookUseCase
import ru.adavydova.booksmart.domain.usecase.books_local.GetLocalBookByIdUseCase
import ru.adavydova.booksmart.domain.usecase.books_local.GetLocalBooksUseCase
import ru.adavydova.booksmart.domain.usecase.books_local.InsertBookUseCase
import ru.adavydova.booksmart.domain.usecase.books_remote.BooksRemoteUseCase
import ru.adavydova.booksmart.domain.usecase.books_remote.FilterBooksUseCase
import ru.adavydova.booksmart.domain.usecase.books_remote.GetBookByIdUseCase
import ru.adavydova.booksmart.domain.usecase.books_remote.SearchBookUseCase
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class BookModule {


    @Singleton
    @Provides
    fun provideDataBase(
        application: Application
    ): BookFavoriteDataBase {
        return Room.databaseBuilder(
            context = application,
            klass = BookFavoriteDataBase::class.java,
            name = BookFavoriteDataBase.NAME_DB
        ).build()
    }

    @Singleton
    @Provides
    fun provideLocalBookRepository(bookFavoriteDataBase: BookFavoriteDataBase): BooksLocalRepository {
        return BooksLocalRepositoryImpl(dao = bookFavoriteDataBase.dao)
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
    fun provideBooksRepository(booksApi: BooksApi): BooksRemoteRepository {
        return BooksRemoteRepositoryImpl(booksApi)
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
    fun provideBooksRemoteUseCase(booksRemoteRepository: BooksRemoteRepository): BooksRemoteUseCase {
        return BooksRemoteUseCase(
            searchBookUseCase = SearchBookUseCase(booksRemoteRepository),
            filterBooksUseCase = FilterBooksUseCase(booksRemoteRepository),
            getBookByIdUseCase = GetBookByIdUseCase(booksRemoteRepository)
        )
    }

    @Singleton
    @Provides
    fun provideBooksLocalUseCase(booksLocalRepository: BooksLocalRepository): BooksLocalUseCase {
        return BooksLocalUseCase(
            getLocalBookByIdUseCase = GetLocalBookByIdUseCase(booksLocalRepository),
            insertBookUseCase = InsertBookUseCase(booksLocalRepository),
            deleteLocalBookUseCase = DeleteLocalBookUseCase(booksLocalRepository),
            getLocalBooksUseCase = GetLocalBooksUseCase(booksLocalRepository)
        )
    }
}