package ru.adavydova.booksmart.presentation.screens.reader_screen

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.readium.adapter.exoplayer.audio.ExoPlayerPreferences
import org.readium.adapter.exoplayer.audio.ExoPlayerSettings
import org.readium.r2.navigator.epub.EpubPreferences
import org.readium.r2.navigator.epub.EpubSettings
import org.readium.r2.navigator.preferences.Configurable
import org.readium.r2.navigator.preferences.PreferencesEditor
import org.readium.r2.shared.ExperimentalReadiumApi
import org.readium.r2.shared.InternalReadiumApi
import org.readium.r2.shared.extensions.mapStateIn
import ru.adavydova.booksmart.reader.PreferencesManager
import ru.adavydova.booksmart.reader.ReaderInitData
import ru.adavydova.booksmart.reader.VisualReaderInitData

@OptIn(ExperimentalReadiumApi::class)
class UserPreferences<S : Configurable.Settings, P : Configurable.Preferences<P>>(
    private val bookId: Long,
    private val preferencesManager: PreferencesManager<P>,
    private val createPreferencesEditor: (P) -> PreferencesEditor<P>
) {
    val scope = CoroutineScope(Dispatchers.Default)

    @OptIn(InternalReadiumApi::class)
    val editor: StateFlow<PreferencesEditor<P>> = preferencesManager.preferences
        .mapStateIn(scope, createPreferencesEditor)

    fun bind(configurable: Configurable<S, P>, lifecycleOwner: LifecycleOwner) {
        with(lifecycleOwner) {
            preferencesManager.preferences
                .flowWithLifecycle(lifecycle)
                .onEach { configurable.submitPreferences(it) }
                .launchIn(lifecycleOwner.lifecycleScope)
        }
    }

    fun commit() {
        scope.launch {
            preferencesManager.setPreferences(editor.value.preferences)
        }
    }

    companion object {

        operator fun invoke(
            readerInitData: ReaderInitData
        ): UserPreferences<*, *>? =
            when (readerInitData) {
                is VisualReaderInitData.EpubReaderInitData -> with(readerInitData) {
                    UserPreferences<EpubSettings, EpubPreferences>(
                        bookId,
                        preferencesManager,
                        createPreferencesEditor = navigatorFactory::createPreferencesEditor
                    )
                }

//                is VisualReaderInitData.PdfReaderInitData -> with(readerInitData) {
//                    TODO()
////                    UserPreferences<PdfiumSettings, PdfiumPreferences>(
////                        bookId,
////                        preferencesManager,
////                        createPreferencesEditor = navigatorFactory::createPreferencesEditor
////                    )
//                }

                is VisualReaderInitData.MediaReaderInitData -> with(readerInitData) {
                    UserPreferences<ExoPlayerSettings, ExoPlayerPreferences>(
                        bookId,
                        preferencesManager,
                        createPreferencesEditor = navigatorFactory::createAudioPreferencesEditor
                    )
                }

                else -> null
            }
    }
}

