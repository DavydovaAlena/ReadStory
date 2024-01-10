package ru.adavydova.booksmart.di


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.adavydova.booksmart.data.remote.BooksApi
import ru.adavydova.booksmart.data.remote.repository.BooksRepositoryImpl
import ru.adavydova.booksmart.domain.repository.BooksRepository
import ru.adavydova.booksmart.domain.usecase.BooksUseCase
import ru.adavydova.booksmart.domain.usecase.SearchBookUseCase
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {


    @Singleton
    @Provides
    fun provideBooksApi(): BooksApi{
        return Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/books/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BooksApi::class.java)
    }



    @Singleton
    @Provides
    fun provideBooksRepository(booksApi: BooksApi): BooksRepository{
        return BooksRepositoryImpl(booksApi)
    }



    @Singleton
    @Provides
    fun provideBooksUseCase(booksRepository: BooksRepository): BooksUseCase{
    return BooksUseCase(
        searchBookUseCase = SearchBookUseCase(booksRepository)
    )
    }
}