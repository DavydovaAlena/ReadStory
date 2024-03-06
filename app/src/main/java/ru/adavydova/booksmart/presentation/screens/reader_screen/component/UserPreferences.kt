package ru.adavydova.booksmart.presentation.screens.reader_screen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.readium.r2.navigator.epub.EpubPreferencesEditor
import org.readium.r2.navigator.preferences.Axis
import org.readium.r2.navigator.preferences.Color
import org.readium.r2.navigator.preferences.ColumnCount
import org.readium.r2.navigator.preferences.Configurable
import org.readium.r2.navigator.preferences.EnumPreference
import org.readium.r2.navigator.preferences.Fit
import org.readium.r2.navigator.preferences.FontFamily
import org.readium.r2.navigator.preferences.ImageFilter
import org.readium.r2.navigator.preferences.Preference
import org.readium.r2.navigator.preferences.PreferencesEditor
import org.readium.r2.navigator.preferences.RangePreference
import org.readium.r2.navigator.preferences.ReadingProgression
import org.readium.r2.navigator.preferences.Spread
import org.readium.r2.navigator.preferences.Theme
import org.readium.r2.navigator.preferences.withSupportedValues
import org.readium.r2.shared.ExperimentalReadiumApi
import org.readium.r2.shared.publication.epub.EpubLayout
import org.readium.r2.shared.util.Language
import ru.adavydova.booksmart.presentation.component.button.ButtonGroupItem
import ru.adavydova.booksmart.presentation.component.button.PresetsMenuButton
import ru.adavydova.booksmart.presentation.component.button.presets
import ru.adavydova.booksmart.presentation.component.menu_setting_item.ColorItem
import ru.adavydova.booksmart.presentation.component.menu_setting_item.MenuItem
import ru.adavydova.booksmart.presentation.component.menu_setting_item.StepperItem
import ru.adavydova.booksmart.presentation.component.menu_setting_item.SwitchItem
import ru.adavydova.booksmart.presentation.screens.reader_screen.UserPreferences

@OptIn(ExperimentalReadiumApi::class)
@Composable
fun UserPreferences(
    model: UserPreferences<*, *>,
    title: String
) {
//    val editor: PreferencesEditor<out Configurable.Preferences<*>>? by model.editor.collectAsState()
    val editor = remember {
        mutableStateOf(model.editor.value)
    }
    val context = LocalContext.current
    UserPreferences(
        editor = editor.value,
        commit = model::commit,
        title = title
    )

}


@OptIn(ExperimentalReadiumApi::class)
@Composable
fun <P : Configurable.Preferences<P>, E : PreferencesEditor<P>> UserPreferences(
    editor: E,
    commit: () -> Unit,
    title: String
) {

    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxWidth()
    ) {
        Text(
            text = title,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
        )

        Row(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.End),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PresetsMenuButton(presets = editor.presets, commit = commit, clear = editor::clear)

            Button(
                onClick = { editor.clear(); commit() }) {
                Text(text = "Reset")
            }
        }

        Divider()

        when (editor) {
//            is PdfiumPreferencesEditor -> {
//                FixedLayoutUserPreferences(
//                    commit = commit,
//                    readingProgression = editor.readingProgression,
//                    scrollAxis = editor.scrollAxis,
//                    fit = editor.fit,
//                    pageSpacing = editor.pageSpacing
//                )
//            }

            is EpubPreferencesEditor -> {
                when (editor.layout) {
                    EpubLayout.REFLOWABLE ->
                        ReflowableUserPreferences(
                            commit = commit,
                            backgroundColor = editor.backgroundColor,
                            columnCount = editor.columnCount,
                            fontFamily = editor.fontFamily,
                            fontSize = editor.fontSize,
                            fontWeight = editor.fontWeight,
                            hyphens = editor.hyphens,
                            imageFilter = editor.imageFilter,
                            language = editor.language,
                            letterSpacing = editor.letterSpacing,
                            ligatures = editor.ligatures,
                            lineHeight = editor.lineHeight,
                            pageMargins = editor.pageMargins,
                            paragraphIndent = editor.paragraphIndent,
                            paragraphSpacing = editor.paragraphSpacing,
                            publisherStyles = editor.publisherStyles,
                            readingProgression = editor.readingProgression,
                            scroll = editor.scroll,
                            textAlign = editor.textAlign,
                            textColor = editor.textColor,
                            textNormalization = editor.textNormalization,
                            theme = editor.theme,
                            typeScale = editor.typeScale,
                            verticalText = editor.verticalText,
                            wordSpacing = editor.wordSpacing
                        )

                    EpubLayout.FIXED -> {
                        FixedLayoutUserPreferences(
                            commit = commit,
                            backgroundColor = editor.backgroundColor,
                            language = editor.language,
                            readingProgression = editor.readingProgression,
                            spread = editor.spread
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
    }

}

@OptIn(ExperimentalReadiumApi::class)
@Composable
fun FixedLayoutUserPreferences(
    commit: () -> Unit,
    language: Preference<Language?>? = null,
    readingProgression: EnumPreference<ReadingProgression>? = null,
    backgroundColor: Preference<Color>? = null,
    scroll: Preference<Boolean>? = null,
    scrollAxis: EnumPreference<Axis>? = null,
    fit: EnumPreference<Fit>? = null,
    spread: EnumPreference<Spread>? = null,
    offsetFirstPage: Preference<Boolean>? = null,
    pageSpacing: RangePreference<Double>? = null
) {
    if (language != null || readingProgression != null) {
//        if (language != null) {
//            LanguageItem(
//                preference = language,
//                commit = commit
//            )
//        }

        if (readingProgression != null) {
            ButtonGroupItem(
                title = "Reading progression",
                preference = readingProgression,
                commit = commit,
                formatValue = { it.name }
            )
        }

        Divider()
    }

    if (backgroundColor != null) {
        ColorItem(
            title = "Background color",
            preference = backgroundColor,
            commit = commit
        )
        Divider()
    }

    if (scroll != null) {
        SwitchItem(
            title = "Scroll",
            preference = scroll,
            commit = commit
        )
    }

    if (scrollAxis != null) {
        ButtonGroupItem(
            title = "Scroll axis",
            preference = scrollAxis,
            commit = commit
        ) { value ->
            when (value) {
                Axis.HORIZONTAL -> "Horizontal"
                Axis.VERTICAL -> "Vertical"
            }
        }
    }

    if (spread != null) {
        ButtonGroupItem(
            title = "Spread",
            preference = spread,
            commit = commit
        ) { value ->
            when (value) {
                Spread.AUTO -> "Auto"
                Spread.NEVER -> "Never"
                Spread.ALWAYS -> "Always"
            }
        }

        if (offsetFirstPage != null) {
            SwitchItem(
                title = "Offset",
                preference = offsetFirstPage,
                commit = commit
            )
        }
    }

    if (fit != null) {
        ButtonGroupItem(
            title = "Fit",
            preference = fit,
            commit = commit
        ) { value ->
            when (value) {
                Fit.CONTAIN -> "Contain"
                Fit.COVER -> "Cover"
                Fit.WIDTH -> "Width"
                Fit.HEIGHT -> "Height"
            }
        }
    }

    if (pageSpacing != null) {
        StepperItem(
            title = "Page spacing",
            preference = pageSpacing,
            commit = commit
        )
    }

}


/**
 * User settings for a publication with adjustable fonts and dimensions, such as
 * a reflowable EPUB, HTML document or PDF with reflow mode enabled.
 */
@OptIn(ExperimentalReadiumApi::class)
@Composable
private fun ReflowableUserPreferences(
    commit: () -> Unit,
    backgroundColor: Preference<Color>? = null,
    columnCount: EnumPreference<ColumnCount>? = null,
    fontFamily: Preference<FontFamily?>? = null,
    fontSize: RangePreference<Double>? = null,
    fontWeight: RangePreference<Double>? = null,
    hyphens: Preference<Boolean>? = null,
    imageFilter: EnumPreference<ImageFilter?>? = null,
    language: Preference<Language?>? = null,
    letterSpacing: RangePreference<Double>? = null,
    ligatures: Preference<Boolean>? = null,
    lineHeight: RangePreference<Double>? = null,
    pageMargins: RangePreference<Double>? = null,
    paragraphIndent: RangePreference<Double>? = null,
    paragraphSpacing: RangePreference<Double>? = null,
    publisherStyles: Preference<Boolean>? = null,
    readingProgression: EnumPreference<ReadingProgression>? = null,
    scroll: Preference<Boolean>? = null,
    textAlign: EnumPreference<org.readium.r2.navigator.preferences.TextAlign?>? = null,
    textColor: Preference<Color>? = null,
    textNormalization: Preference<Boolean>? = null,
    theme: EnumPreference<Theme>? = null,
    typeScale: RangePreference<Double>? = null,
    verticalText: Preference<Boolean>? = null,
    wordSpacing: RangePreference<Double>? = null
) {
    if (language != null || readingProgression != null || verticalText != null) {
//        if (language != null) {
//            LanguageItem(
//                preference = language,
//                commit = commit
//            )
//        }

        if (readingProgression != null) {
            ButtonGroupItem(
                title = "Reading progression",
                preference = readingProgression,
                commit = commit,
                formatValue = { it.name }
            )
        }

        if (verticalText != null) {
            SwitchItem(
                title = "Vertical text",
                preference = verticalText,
                commit = commit
            )
        }

        Divider()
    }

    if (scroll != null || columnCount != null || pageMargins != null) {


        if (scroll != null) {
            SwitchItem(
                title = "Scroll",
                preference = scroll,
                commit = commit
            )
        }

        if (columnCount != null) {
            ButtonGroupItem(
                title = "Columns",
                preference = columnCount,
                commit = commit
            ) { value ->
                when (value) {
                    ColumnCount.AUTO -> "Auto"
                    ColumnCount.ONE -> "1"
                    ColumnCount.TWO -> "2"
                }
            }
        }

        if (pageMargins != null) {
            StepperItem(
                title = "Page margins",
                preference = pageMargins,
                commit = commit
            )
        }

        Divider()
    }

    if (theme != null || textColor != null || imageFilter != null) {
        if (theme != null) {
            ButtonGroupItem(
                title = "Theme",
                preference = theme,
                commit = commit
            ) { value ->
                when (value) {
                    Theme.LIGHT -> "Light"
                    Theme.DARK -> "Dark"
                    Theme.SEPIA -> "Sepia"
                }
            }
        }

        if (imageFilter != null) {
            ButtonGroupItem(
                title = "Image filter",
                preference = imageFilter,
                commit = commit
            ) { value ->
                when (value) {
                    ImageFilter.DARKEN -> "Darken"
                    ImageFilter.INVERT -> "Invert"
                    null -> "None"
                }
            }
        }

        if (textColor != null) {
            ColorItem(
                title = "Text color",
                preference = textColor,
                commit = commit
            )
        }

        if (backgroundColor != null) {
            ColorItem(
                title = "Background color",
                preference = backgroundColor,
                commit = commit
            )
        }

        Divider()
    }

    if (fontFamily != null || fontSize != null || textNormalization != null) {
        if (fontFamily != null) {
            MenuItem(
                title = "Typeface",
                preference = fontFamily
                    .withSupportedValues(
                        null,
                        FontFamily.LITERATA,
                        FontFamily.SANS_SERIF,
                        FontFamily.IA_WRITER_DUOSPACE,
                        FontFamily.ACCESSIBLE_DFA,
                        FontFamily.OPEN_DYSLEXIC
                    ),
                commit = commit
            ) { value ->
                when (value) {
                    null -> "Original"
                    FontFamily.SANS_SERIF -> "Sans Serif"
                    else -> value.name
                }
            }
        }

        if (fontSize != null) {
            StepperItem(
                title = "Font size",
                preference = fontSize,
                commit = commit
            )
        }

        if (fontWeight != null) {
            StepperItem(
                title = "Font weight",
                preference = fontWeight,
                commit = commit
            )
        }

        if (textNormalization != null) {
            SwitchItem(
                title = "Text normalization",
                preference = textNormalization,
                commit = commit
            )
        }

        Divider()
    }

    if (publisherStyles != null) {
        SwitchItem(
            title = "Publisher styles",
            preference = publisherStyles,
            commit = commit
        )

        if (!(publisherStyles.value ?: publisherStyles.effectiveValue)) {
            if (textAlign != null) {
                ButtonGroupItem(
                    title = "Alignment",
                    preference = textAlign,
                    commit = commit
                ) { value ->
                    when (value) {
                        org.readium.r2.navigator.preferences.TextAlign.CENTER -> "Center"
                        org.readium.r2.navigator.preferences.TextAlign.JUSTIFY -> "Justify"
                        org.readium.r2.navigator.preferences.TextAlign.START -> "Start"
                        org.readium.r2.navigator.preferences.TextAlign.END -> "End"
                        org.readium.r2.navigator.preferences.TextAlign.LEFT -> "Left"
                        org.readium.r2.navigator.preferences.TextAlign.RIGHT -> "Right"
                        null -> "Default"
                    }
                }
            }

            if (typeScale != null) {
                StepperItem(
                    title = "Type scale",
                    preference = typeScale,
                    commit = commit
                )
            }

            if (lineHeight != null) {
                StepperItem(
                    title = "Line height",
                    preference = lineHeight,
                    commit = commit
                )
            }

            if (paragraphIndent != null) {
                StepperItem(
                    title = "Paragraph indent",
                    preference = paragraphIndent,
                    commit = commit
                )
            }

            if (paragraphSpacing != null) {
                StepperItem(
                    title = "Paragraph spacing",
                    preference = paragraphSpacing,
                    commit = commit
                )
            }

            if (wordSpacing != null) {
                StepperItem(
                    title = "Word spacing",
                    preference = wordSpacing,
                    commit = commit
                )
            }

            if (letterSpacing != null) {
                StepperItem(
                    title = "Letter spacing",
                    preference = letterSpacing,
                    commit = commit
                )
            }

            if (hyphens != null) {
                SwitchItem(
                    title = "Hyphens",
                    preference = hyphens,
                    commit = commit
                )
            }

            if (ligatures != null) {
                SwitchItem(
                    title = "Ligatures",
                    preference = ligatures,
                    commit = commit
                )
            }
        }
    }
}

@OptIn(ExperimentalReadiumApi::class)
val FontFamily.Companion.LITERATA: FontFamily get() = FontFamily("Literata")


@Composable
fun Divider() {
    androidx.compose.material3.Divider(modifier = Modifier.padding(vertical = 16.dp))
}