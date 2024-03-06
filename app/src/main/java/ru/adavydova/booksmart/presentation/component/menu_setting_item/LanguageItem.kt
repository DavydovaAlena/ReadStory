package ru.adavydova.booksmart.presentation.component.menu_setting_item

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.readium.r2.navigator.preferences.Preference
import org.readium.r2.navigator.preferences.withSupportedValues
import org.readium.r2.shared.ExperimentalReadiumApi
import org.readium.r2.shared.util.Language
import java.util.Locale

@OptIn(ExperimentalReadiumApi::class)
@Composable
fun LanguageItem(
    preference: Preference<Language?>,
    commit: ()-> Unit
) {
    val languages = remember {
        Locale.getAvailableLocales()
            .map { Language(it).removeRegion() }
            .distinct()
            .sortedBy { it.locale.displayName }
    }

    MenuItem(
        title = "Language",
        preference = preference.withSupportedValues(languages + null),
        formatValue = { it?.locale?.displayName ?: "Unknown" },
        commit = commit
    )

}