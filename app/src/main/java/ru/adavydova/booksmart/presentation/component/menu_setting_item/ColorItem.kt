package ru.adavydova.booksmart.presentation.component.menu_setting_item

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.window.Dialog
import org.readium.r2.navigator.preferences.Preference
import org.readium.r2.navigator.preferences.clear
import org.readium.r2.shared.ExperimentalReadiumApi
import ru.adavydova.booksmart.R
import ru.adavydova.booksmart.presentation.component.colore_picker.ColorPicker


/**
 * Component for a [Preference<ReadiumColor>].
 */
@OptIn(ExperimentalReadiumApi::class)
@Composable
fun ColorItem(
    title: String,
    preference: Preference<org.readium.r2.navigator.preferences.Color>,
    commit: () -> Unit
) {
    ColorItem(
        title = title,
        isActive = preference.isEffective,
        value = preference.value ?: preference.effectiveValue,
        noValueSelected = preference.value == null,
        onColorChanged = { preference.set(it); commit() },
        onClear = { preference.clear(); commit() }
            .takeIf { preference.value != null }
    )
}

/**
 * Color picker
 */
@OptIn(ExperimentalReadiumApi::class)
@Composable
private fun ColorItem(
    title: String,
    isActive: Boolean,
    value: org.readium.r2.navigator.preferences.Color,
    noValueSelected: Boolean,
    onColorChanged: (org.readium.r2.navigator.preferences.Color?) -> Unit,
    onClear: (() -> Unit)?
) {
    var isPicking by remember { mutableStateOf(false) }

    ItemMenu(
        title = title,
        isActive = isActive,
        onClick = { isPicking = true },
        onClear = onClear
    ) {
        val color = Color(value.int)

        OutlinedButton(
            onClick = { isPicking = true },
            colors = ButtonDefaults.buttonColors(containerColor = color)
        ) {
            if (noValueSelected) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_palette_24),
                    contentDescription = "Change color",
                    tint = if (color.luminance() > 0.5) androidx.compose.ui.graphics.Color.Black else androidx.compose.ui.graphics.Color.White
                )
            }
        }
        if (isPicking) {
            Dialog(
                onDismissRequest = { isPicking = false }
            ) {
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    ColorPicker(
                        onPick = {
                            isPicking = false
                            onColorChanged(org.readium.r2.navigator.preferences.Color(it))
                        },
                        closeMenu = {
                            isPicking = false
                            onColorChanged(null)
                        })
                }
            }
        }
    }
}
