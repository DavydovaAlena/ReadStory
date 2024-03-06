package ru.adavydova.booksmart.presentation.component.menu_setting_item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import org.readium.r2.navigator.preferences.EnumPreference
import org.readium.r2.navigator.preferences.clear
import org.readium.r2.shared.ExperimentalReadiumApi
import ru.adavydova.booksmart.R
import ru.adavydova.booksmart.presentation.component.button.DropDownMenuButton

@OptIn(ExperimentalReadiumApi::class)
@Composable
fun <T> MenuItem(
    title: String,
    preference: EnumPreference<T>,
    commit: () -> Unit,
    formatValue: (T) -> String
) {
    MenuItem(
        title = title,
        value = preference.value ?: preference.effectiveValue,
        values = preference.supportedValues,
        isActive = preference.isEffective,
        formatValue = formatValue,
        onClear = { preference.clear(); commit() }
            .takeIf { preference.value != null },
        onValueChanged = { value ->
            preference.set(value)
            commit()
        }
    )
}

@Composable
fun <T> MenuItem(
    title: String,
    value: T,
    values: List<T>,
    isActive: Boolean,
    formatValue: (T) -> String,
    onValueChanged: (T) -> Unit,
    onClear: (() -> Unit)?
) {
    ItemMenu(title, isActive = isActive, onClear = onClear) {
        DropDownMenuButton(
            text = {
                Text(
                    text = formatValue(value),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        ) { dismiss ->
            for (aValue in values) {
                DropdownMenuItem(
                    text = {
                        Text(formatValue(aValue))
                    },
                    onClick = {
                        dismiss()
                        onValueChanged(aValue)
                    })
            }
        }
    }
}


@Composable
fun ItemMenu(
    title: String,
    isActive: Boolean = true,
    onClick: (() -> Unit)? = null,
    onClear: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {

    ListItem(
        modifier = if (onClick != null) {
            Modifier.clickable(onClick = onClick)
        } else {
            Modifier
        },
        headlineContent = {
            Text(
                modifier = Modifier.alpha(
                    if (isActive) 1.0f else 0.33f
                ),
                text = title
            )
        },
        trailingContent = {
            Row {
                content()

                IconButton(onClick = onClear ?: {}, enabled = onClear != null) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.format_clear_svgrepo_com),
                        contentDescription = "Clear"
                    )
                }
            }
        })
}