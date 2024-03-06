package ru.adavydova.booksmart.presentation.component.button

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.readium.r2.navigator.epub.EpubPreferences
import org.readium.r2.navigator.epub.EpubPreferencesEditor
import org.readium.r2.navigator.preferences.Configurable
import org.readium.r2.navigator.preferences.PreferencesEditor
import org.readium.r2.navigator.preferences.ReadingProgression
import org.readium.r2.shared.ExperimentalReadiumApi
import org.readium.r2.shared.publication.epub.EpubLayout

@Composable
fun PresetsMenuButton(
    presets: List<Preset>,
    clear: () -> Unit,
    commit: () -> Unit
) {
    if (presets.isEmpty()) return

    DropDownMenuButton(
        text = {
            Text(text = "Presets")
        }) { dismiss ->

        for (preset in presets) {
            DropdownMenuItem(
                text = {
                   Text(text = preset.title)
                },
                onClick = {
                    dismiss()
                    clear()
                    preset.apply()
                    commit()
                }
            )
        }
    }
}

class Preset(
    val title: String,
    val apply: () -> Unit
)

@OptIn(ExperimentalReadiumApi::class)
val <P: Configurable.Preferences<P>> PreferencesEditor <P>.presets : List<Preset> get() =
    when(this){
        is EpubPreferencesEditor ->
            when(layout){
                EpubLayout.FIXED -> emptyList()
                EpubLayout.REFLOWABLE -> listOf(
                    Preset("Increase legibility") {
                        wordSpacing.set(0.6)
                        fontSize.set(1.4)
                        fontWeight.set(2.0)
                    },
                    Preset("Document") {
                        scroll.set(true)
                    },
                    Preset("Ebook") {
                        scroll.set(false)
                    },
                    Preset("Manga") {
                        scroll.set(false)
                        readingProgression.set(ReadingProgression.RTL)
                    }
                )
            }
        else -> emptyList()
    }