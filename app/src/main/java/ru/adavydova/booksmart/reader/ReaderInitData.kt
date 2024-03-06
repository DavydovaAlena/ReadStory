package ru.adavydova.booksmart.reader


import org.readium.adapter.exoplayer.audio.ExoPlayerNavigator
import org.readium.adapter.exoplayer.audio.ExoPlayerNavigatorFactory
import org.readium.adapter.exoplayer.audio.ExoPlayerPreferences
import org.readium.navigator.media.tts.AndroidTtsNavigatorFactory
import org.readium.navigator.media.tts.android.AndroidTtsPreferences
import org.readium.r2.navigator.epub.EpubNavigatorFactory
import org.readium.r2.navigator.epub.EpubPreferences
import org.readium.r2.shared.ExperimentalReadiumApi
import org.readium.r2.shared.publication.Locator
import org.readium.r2.shared.publication.Manifest
import org.readium.r2.shared.publication.Metadata
import org.readium.r2.shared.publication.Publication

sealed class ReaderInitData(
    val bookId: Long,
    val publication: Publication
)

sealed class VisualReaderInitData(
    bookId: Long,
    publication: Publication,
    val initialLocation: Locator?,
    val ttsInitData: TtsInitData?
) : ReaderInitData(bookId, publication) {

    class ImageReaderInitData(
        bookId: Long,
        publication: Publication,
        initialLocation: Locator?,
        ttsInitData: TtsInitData?,
    ) : VisualReaderInitData(bookId, publication, initialLocation, ttsInitData)


    class EpubReaderInitData @OptIn(ExperimentalReadiumApi::class) constructor(
        bookId: Long,
        publication: Publication,
        initialLocation: Locator?,
        val preferencesManager: PreferencesManager<EpubPreferences>,
        val navigatorFactory: EpubNavigatorFactory,
        ttsInitData: TtsInitData?
    ) : VisualReaderInitData(bookId, publication, initialLocation, ttsInitData)


//    class PdfReaderInitData @OptIn(ExperimentalReadiumApi::class) constructor(
//        bookId: Long,
//        publication: Publication,
//        initialLocation: Locator?,
//        val preferencesManager: PreferencesManager<PdfiumPreferences>,
//        val navigatorFactory: PdfiumNavigatorFactory,
//        ttsInitData: TtsInitData?
//    ) : VisualReaderInitData(bookId, publication, initialLocation, ttsInitData)

    class MediaReaderInitData @OptIn(ExperimentalReadiumApi::class) constructor(
        bookId: Long,
        publication: Publication,
        val mediaNavigator: ExoPlayerNavigator,
        val preferencesManager: PreferencesManager<ExoPlayerPreferences>,
        val navigatorFactory: ExoPlayerNavigatorFactory
    ) : ReaderInitData(bookId, publication)


}


class TtsInitData @OptIn(ExperimentalReadiumApi::class) constructor(
    val navigatorFactory: AndroidTtsNavigatorFactory,
    val preferencesManager: PreferencesManager<AndroidTtsPreferences>
)

class DummyReaderInitData(
    bookId: Long,
) : ReaderInitData(
    bookId,
    publication = Publication(
        Manifest(metadata = Metadata(identifier = "dummy"))
    )
)