package ru.adavydova.booksmart.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.readium.r2.shared.util.asset.AssetRetriever
import org.readium.r2.shared.util.downloads.android.AndroidDownloadManager
import org.readium.r2.shared.util.http.DefaultHttpClient
import org.readium.r2.shared.util.http.HttpClient
import org.readium.r2.streamer.PublicationOpener
import ru.adavydova.booksmart.Readium
import ru.adavydova.booksmart.data.local.book.BookDataBase
import ru.adavydova.booksmart.data.local.book.DownloadDao
import ru.adavydova.booksmart.data.local.book.ReadiumBooksDao
import ru.adavydova.booksmart.data.local.repository.DownloadReadiumBookRepositoryImpl
import ru.adavydova.booksmart.data.local.repository.ReaderRepositoryImpl
import ru.adavydova.booksmart.data.local.repository.ReadiumBooksRepositoryImpl
import ru.adavydova.booksmart.domain.CoverStorage
import ru.adavydova.booksmart.domain.model.readium_book.Download
import ru.adavydova.booksmart.domain.model.readium_book.HighlightConverters
import ru.adavydova.booksmart.domain.repository.DownloadReadiumBookRepository
import ru.adavydova.booksmart.domain.repository.ReaderRepository
import ru.adavydova.booksmart.domain.repository.ReadiumBooksRepository
import ru.adavydova.booksmart.domain.usecase.readium_books.ReadiumBookUseCase
import ru.adavydova.booksmart.navigatorPreferences
import ru.adavydova.booksmart.reader.ComputeStorageDir
import ru.adavydova.booksmart.reader.CoroutineQueue
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ReadiumBookModule {



    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient{
        return DefaultHttpClient()
    }

    @Provides
    @Singleton
    fun provideAndroidDownloadManager(
        application: Application): AndroidDownloadManager {
        return AndroidDownloadManager(
            application,
            destStorage = AndroidDownloadManager.Storage.App)
    }


    @Provides
    @Singleton
    fun providePublicationOpener(readium: Readium): PublicationOpener{
        return readium.publicationOpener
    }


    @Provides
    @Singleton
    fun provideAssetsRetriever(
        application: Application,
        httpClient: HttpClient
    ): AssetRetriever{
        return AssetRetriever(application.contentResolver, httpClient)
    }


    @Provides
    @Singleton
    fun provideComputeDir(application: Application): ComputeStorageDir{
        return ComputeStorageDir(application)
    }

    @Provides
    @Singleton
    fun provideReadiumBookDataBase(
        application: Application
    ): BookDataBase {
        return Room.databaseBuilder(
            context = application,
            klass = BookDataBase::class.java,
            name = BookDataBase.NAME_DATABASE
        )
            .addTypeConverter(HighlightConverters())
            .addTypeConverter(Download.Type.Converter())
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideReadiumBooksDao(
        bookDataBase: BookDataBase
    ): ReadiumBooksDao {
        return bookDataBase.readiumBookDao
    }

    @Provides
    @Singleton
    fun provideDownloadDao(
        bookDataBase: BookDataBase
    ): DownloadDao {
        return bookDataBase.downloadDao
    }

    @Provides
    @Singleton
    fun provideReadiumBooksRepository(
        application: Application,
        readiumBooksDao: ReadiumBooksDao
    ): ReadiumBooksRepository {
        return ReadiumBooksRepositoryImpl(
            context = application,
            bookDao = readiumBooksDao
        )
    }


    @Singleton
    @Provides
    fun provideReadium(
        application: Application,
        httpClient: HttpClient,
        downloadManager: AndroidDownloadManager,
        assetRetriever: AssetRetriever
    ): Readium {
        return Readium(application,httpClient, downloadManager, assetRetriever)
    }

    @Singleton
    @Provides
    fun provideCoverStorage(
        readium: Readium,
        computeStorageDir: ComputeStorageDir,
    ): CoverStorage {
        return CoverStorage(
            appStorageDir = computeStorageDir,
            httpClient = readium.httpClient
        )
    }

    @Singleton
    @Provides
    fun provideDownloadRepository(
        downloadDao: DownloadDao
    ): DownloadReadiumBookRepository {
        return DownloadReadiumBookRepositoryImpl(
            downloadDao = downloadDao,
            type = Download.Type.LSP
        )
    }

    @Singleton
    @Provides
    fun provideDataStore(application: Application): DataStore<Preferences> {
        return application.navigatorPreferences
    }

    @Singleton
    @Provides
    fun provideReaderRepository(
        application: Application,
        readium: Readium,
        useCase: ReadiumBookUseCase,
        dataStore: DataStore<Preferences>,
        coroutineQueue: CoroutineQueue
    ): ReaderRepository {
        return ReaderRepositoryImpl(
            application,
            readium,
            useCase,
            dataStore,
            coroutineQueue
        )
    }



}