package ru.adavydova.booksmart.data.local.repository

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.json.JSONObject
import org.readium.adapter.exoplayer.audio.ExoPlayerEngineProvider
import org.readium.navigator.media.audio.AudioNavigatorFactory
import org.readium.navigator.media.tts.TtsNavigatorFactory
import org.readium.r2.navigator.epub.EpubNavigatorFactory
import org.readium.r2.shared.ExperimentalReadiumApi
import org.readium.r2.shared.publication.Locator
import org.readium.r2.shared.publication.Publication
import org.readium.r2.shared.publication.allAreHtml
import org.readium.r2.shared.publication.services.isRestricted
import org.readium.r2.shared.publication.services.protectionError
import org.readium.r2.shared.util.DebugError
import org.readium.r2.shared.util.Try
import org.readium.r2.shared.util.getOrElse
import ru.adavydova.booksmart.Readium
import ru.adavydova.booksmart.domain.exception_handling.PublicationError
import ru.adavydova.booksmart.domain.repository.ReaderRepository
import ru.adavydova.booksmart.domain.usecase.readium_books.ReadiumBookUseCase
import ru.adavydova.booksmart.reader.AndroidTtsPreferencesManagerFactory
import ru.adavydova.booksmart.reader.CoroutineQueue
import ru.adavydova.booksmart.reader.EpubPreferencesManagerFactory
import ru.adavydova.booksmart.reader.ExoPlayerPreferencesManagerFactory
import ru.adavydova.booksmart.reader.OpeningError
import ru.adavydova.booksmart.reader.ReaderInitData
import ru.adavydova.booksmart.reader.TtsInitData
import ru.adavydova.booksmart.reader.VisualReaderInitData
import timber.log.Timber
import javax.inject.Inject

class ReaderRepositoryImpl @Inject constructor(
    private val application: Application,
    private val readium: Readium,
    private val useCase: ReadiumBookUseCase,
    private val preferencesDataStore: DataStore<Preferences>,
    private val coroutineQueue: CoroutineQueue
) : ReaderRepository {


    override suspend fun open(bookId: Long): Try<ReaderInitData, OpeningError> {
        return coroutineQueue.await { doOpen(bookId) }
    }

    private suspend fun doOpen(bookId: Long): Try<ReaderInitData, OpeningError> {

        val book = checkNotNull(useCase.getBookUseCase(bookId)) { "Cannot find book in database." }
        val asset = readium.assetRetriever.retrieve(
            book.url,
            book.mediaType
        ).getOrElse {
            return Try.failure(
                OpeningError.PublicationError(
                    PublicationError(it)
                )
            )
        }

        val publication = readium.publicationOpener.open(
            asset,
            allowUserInteraction = true
        ).getOrElse {
            return Try.failure(
                OpeningError.PublicationError(
                    PublicationError(it)
                )
            )
        }
        // The publication is protected with a DRM and not unlocked.
        if (publication.isRestricted) {
            return Try.failure(
                OpeningError.RestrictedPublication(
                    publication.protectionError
                        ?: DebugError("Publication is restricted.")
                )
            )
        }

        val initialLocator = book.progression
            ?.let { Locator.fromJSON(JSONObject(it)) }

        val readerInitData = when {
            publication.conformsTo(Publication.Profile.AUDIOBOOK) ->
                openAudio(bookId, publication, initialLocator)

            publication.conformsTo(Publication.Profile.EPUB) || publication.readingOrder.allAreHtml ->
                openEpub(bookId, publication, initialLocator)

//            publication.conformsTo(Publication.Profile.PDF) ->
//                openPdf(bookId, publication, initialLocator)

            publication.conformsTo(Publication.Profile.DIVINA) ->
                openImage(bookId, publication, initialLocator)

            else ->
                Try.failure(
                    OpeningError.CannotRender(
                        DebugError("No navigator supports this publication.")
                    )
                )
        }

        Timber.tag(bookId.toString())
        return readerInitData

    }

    private suspend fun openImage(
        bookId: Long,
        publication: Publication,
        initialLocator: Locator?
    ): Try<VisualReaderInitData.ImageReaderInitData, OpeningError> {
        val initData = VisualReaderInitData.ImageReaderInitData(
            bookId = bookId,
            publication = publication,
            initialLocation = initialLocator,
            ttsInitData = getTtsInitData(bookId, publication)
        )
        return Try.success(initData)
    }

    @OptIn(ExperimentalReadiumApi::class)
    private suspend fun openAudio(
        bookId: Long,
        publication: Publication,
        initialLocator: Locator?
    ): Try<VisualReaderInitData.MediaReaderInitData, OpeningError> {


        val preferencesManager = ExoPlayerPreferencesManagerFactory(preferencesDataStore)
            .createPreferenceManager(bookId)
        val initialPreferences = preferencesManager.preferences.value


        val navigatorFactory = AudioNavigatorFactory(
            publication,
            ExoPlayerEngineProvider(application)
        ) ?: return Try.failure(
            OpeningError.CannotRender(
                DebugError("Cannot create audio navigator factory.")
            )
        )

        val navigator = navigatorFactory.createNavigator(
            initialLocator,
            initialPreferences
        ).getOrElse {
            return Try.failure(
                when (it) {
                    is AudioNavigatorFactory.Error.EngineInitialization ->
                        OpeningError.AudioEngineInitialization(it)

                    is AudioNavigatorFactory.Error.UnsupportedPublication ->
                        OpeningError.CannotRender(it)
                }
            )
        }

        val initData = VisualReaderInitData.MediaReaderInitData(
            bookId,
            publication,
            navigator,
            preferencesManager,
            navigatorFactory
        )

        return Try.success(initData)

    }


    @OptIn(ExperimentalReadiumApi::class)
    private suspend fun openEpub(
        bookId: Long,
        publication: Publication,
        initialLocator: Locator?
    ): Try<VisualReaderInitData.EpubReaderInitData, OpeningError> {
        val preferencesManager = EpubPreferencesManagerFactory(preferencesDataStore)
            .createPreferenceManager(bookId)
        val navigatorFactory = EpubNavigatorFactory(publication)
        val ttsInitData = getTtsInitData(bookId, publication)

        val initData = VisualReaderInitData.EpubReaderInitData(
            bookId,
            publication,
            initialLocator,
            preferencesManager,
            navigatorFactory,
            ttsInitData
        )
        Timber.tag("ok").d(System.currentTimeMillis().toString())
        return Try.success(initData)
    }

//    @OptIn(ExperimentalReadiumApi::class)
//    private suspend fun openPdf(
//        bookId: Long,
//        publication: Publication,
//        initialLocator: Locator?
//    ): Try<VisualReaderInitData.PdfReaderInitData, OpeningError> {
//        val preferencesManager = PdfiumPreferencesManagerFactory(preferencesDataStore)
//            .createPreferenceManager(bookId)
//        val pdfEngine = TODO()
//        val navigatorFactory = TODO()
//        val ttsInitData = getTtsInitData(bookId, publication)
//
//        val initData = VisualReaderInitData.PdfReaderInitData(
//            bookId,
//            publication,
//            initialLocator,
//            preferencesManager,
//            navigatorFactory,
//            ttsInitData
//        )
//        return Try.success(initData)
//    }

    @OptIn(ExperimentalReadiumApi::class)
    private suspend fun getTtsInitData(
        bookId: Long,
        publication: Publication
    ): TtsInitData? {
        val preferencesManager = AndroidTtsPreferencesManagerFactory(preferencesDataStore)
            .createPreferenceManager(bookId)
        val navigatorFactory = TtsNavigatorFactory(
            application,
            publication
        ) ?: return null
        return TtsInitData(navigatorFactory, preferencesManager)
    }


}