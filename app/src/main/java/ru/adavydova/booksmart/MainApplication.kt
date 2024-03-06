package ru.adavydova.booksmart

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.HiltAndroidApp
import ru.adavydova.booksmart.domain.Bookshelf
import java.io.File
import java.util.Properties


@HiltAndroidApp
class MainApplication: Application()

val Context.navigatorPreferences: DataStore<Preferences>
        by preferencesDataStore(name = "navigator-preferences")


