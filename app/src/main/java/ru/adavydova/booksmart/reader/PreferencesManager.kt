package ru.adavydova.booksmart.reader

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import org.readium.adapter.exoplayer.audio.ExoPlayerPreferences
import org.readium.adapter.exoplayer.audio.ExoPlayerPreferencesSerializer
import org.readium.navigator.media.tts.android.AndroidTtsPreferences
import org.readium.navigator.media.tts.android.AndroidTtsPreferencesSerializer
import org.readium.navigator.media.tts.android.AndroidTtsPublicationPreferencesFilter
import org.readium.navigator.media.tts.android.AndroidTtsSharedPreferencesFilter
import org.readium.r2.navigator.epub.EpubPreferences
import org.readium.r2.navigator.epub.EpubPreferencesSerializer
import org.readium.r2.navigator.epub.EpubPublicationPreferencesFilter
import org.readium.r2.navigator.epub.EpubSharedPreferencesFilter
import org.readium.r2.navigator.preferences.Configurable
import org.readium.r2.navigator.preferences.PreferencesFilter
import org.readium.r2.navigator.preferences.PreferencesSerializer
import org.readium.r2.shared.ExperimentalReadiumApi
import ru.adavydova.booksmart.util.extensions.stateInFirst
import ru.adavydova.booksmart.util.tryOrNull
import kotlin.reflect.KClass

@OptIn(ExperimentalReadiumApi::class)
class PreferencesManager<P : Configurable.Preferences<P>> internal constructor(
    val preferences: StateFlow<P>,
    @Suppress("Unused") // Keep the scope alive until the PreferencesManager is garbage collected
    private val coroutineScope: CoroutineScope,
    private val editPreferences: suspend (P) -> Unit
) {
    suspend fun setPreferences(preferences: P) {
        editPreferences(preferences)
    }

}

@OptIn(ExperimentalReadiumApi::class)
sealed class PreferencesManagerFactory<P : Configurable.Preferences<P>>(
    private val dataStore: DataStore<Preferences>,
    private val klass: KClass<P>,
    private val sharedPreferencesFilter: PreferencesFilter<P>,
    private val publicationPreferencesFilter: PreferencesFilter<P>,
    private val preferencesSerializer: PreferencesSerializer<P>,
    private val emptyPreferences: P
) {

    suspend fun createPreferenceManager(bookId: Long): PreferencesManager<P> {
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        val preferences = getPreferences(bookId, coroutineScope)
        return PreferencesManager(
            preferences = preferences,
            coroutineScope = coroutineScope,
            editPreferences = { setPreferences(bookId, it) }
        )
    }

    private suspend fun setPreferences(bookId: Long, preferences: P) {
        dataStore.edit { data ->
            data[key(klass)] = sharedPreferencesFilter.filter(preferences).let {
                preferencesSerializer.serialize(it)
            }
        }

        dataStore.edit { data ->
            data[key(bookId)] = publicationPreferencesFilter
                .filter(preferences)
                .let { preferencesSerializer.serialize(it) }
        }

    }

    private suspend fun getPreferences(bookId: Long, scope: CoroutineScope): StateFlow<P> {
        val sharedPrefs = dataStore.data
            .map { data -> data[key(klass)] }
            .map { json ->
                tryOrNull {
                    json?.let { preferencesSerializer.deserialize(it) }
                } ?: emptyPreferences
            }
        val pubPrefs = dataStore.data
            .map { data -> data[key(bookId)] }
            .map { json ->
                tryOrNull {
                    json?.let { preferencesSerializer.deserialize(it) }
                } ?: emptyPreferences
            }
        return combine(sharedPrefs, pubPrefs) { shared, pub -> shared + pub }
            .stateInFirst(scope = scope, SharingStarted.Eagerly)
    }

    private fun key(bookId: Long): Preferences.Key<String> =
        stringPreferencesKey("book-$bookId")

    private fun <T : Any> key(klass: KClass<T>): Preferences.Key<String> =
        stringPreferencesKey("class-${klass.simpleName}")
}

//
//@OptIn(ExperimentalReadiumApi::class)
//class PdfiumPreferencesManagerFactory(
//    dataStore: DataStore<Preferences>
//) : PreferencesManagerFactory<PdfiumPreferences>(
//    dataStore = dataStore,
//    klass = PdfiumPreferences::class,
//    sharedPreferencesFilter = PdfiumSharedPreferencesFilter,
//    publicationPreferencesFilter = PdfiumPublicationPreferencesFilter,
//    preferencesSerializer = PdfiumPreferencesSerializer(),
//    emptyPreferences = PdfiumPreferences()
//)


@OptIn(ExperimentalReadiumApi::class)
class EpubPreferencesManagerFactory(
    dataStore: DataStore<Preferences>
) : PreferencesManagerFactory<EpubPreferences>(
    dataStore = dataStore,
    klass = EpubPreferences::class,
    sharedPreferencesFilter = EpubSharedPreferencesFilter,
    preferencesSerializer = EpubPreferencesSerializer(),
    emptyPreferences = EpubPreferences(),
    publicationPreferencesFilter = EpubPublicationPreferencesFilter
)

@OptIn(ExperimentalReadiumApi::class)
class ExoPlayerPreferencesManagerFactory(
    dataStore: DataStore<Preferences>
) : PreferencesManagerFactory<ExoPlayerPreferences>(
    dataStore = dataStore,
    klass = ExoPlayerPreferences::class,
    sharedPreferencesFilter = { preferences -> preferences },
    publicationPreferencesFilter = { ExoPlayerPreferences() },
    preferencesSerializer = ExoPlayerPreferencesSerializer(),
    emptyPreferences = ExoPlayerPreferences()
)

@OptIn(ExperimentalReadiumApi::class)
class AndroidTtsPreferencesManagerFactory(
    dataStore: DataStore<Preferences>
) : PreferencesManagerFactory<AndroidTtsPreferences>(
    dataStore = dataStore,
    klass = AndroidTtsPreferences::class,
    sharedPreferencesFilter = AndroidTtsSharedPreferencesFilter,
    publicationPreferencesFilter = AndroidTtsPublicationPreferencesFilter,
    preferencesSerializer = AndroidTtsPreferencesSerializer(),
    emptyPreferences = AndroidTtsPreferences()
)
