package ru.adavydova.booksmart.presentation.screens.reader_screen.common

import kotlinx.coroutines.CoroutineScope
import org.readium.navigator.media.tts.AndroidTtsNavigatorFactory
import org.readium.navigator.media.tts.TtsNavigator
import org.readium.navigator.media.tts.android.AndroidTtsPreferences
import org.readium.r2.shared.ExperimentalReadiumApi
import org.readium.r2.shared.publication.Publication
import org.readium.r2.shared.util.Language
import ru.adavydova.booksmart.reader.PreferencesManager
import ru.adavydova.booksmart.reader.ReaderInitData
import ru.adavydova.booksmart.reader.TtsError
import ru.adavydova.booksmart.reader.VisualReaderInitData

@OptIn(ExperimentalReadiumApi::class)
class TtsViewModel @OptIn(ExperimentalReadiumApi::class)
private constructor(
    private val scope: CoroutineScope,
    private val bookId:Long,
    private val publication: Publication,
    private val ttsNavigatorFactory: AndroidTtsNavigatorFactory,
    private val preferencesManager: PreferencesManager<AndroidTtsPreferences>
): TtsNavigator.Listener{


    companion object{
        operator fun invoke(
            scope: CoroutineScope,
            readerInitData: ReaderInitData
        ):TtsViewModel?{
            if (readerInitData !is VisualReaderInitData || readerInitData.ttsInitData == null) {
                return null
            }
            return TtsViewModel(
                scope = scope,
                bookId = readerInitData.bookId,
                publication = readerInitData.publication,
                ttsNavigatorFactory = readerInitData.ttsInitData.navigatorFactory,
                preferencesManager = readerInitData.ttsInitData.preferencesManager
            )
        }
    }

    sealed class Event {
        /**
         * Emitted when the [TtsNavigator] fails with an error.
         */
        class OnError(val error: TtsError) : Event()

        /**
         * Emitted when the selected language cannot be played because it is missing voice data.
         */
        class OnMissingVoiceData(val language: Language) : Event()
    }



    override fun onStopRequested() {
        TODO("Not yet implemented")
    }
}