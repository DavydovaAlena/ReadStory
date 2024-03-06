package ru.adavydova.booksmart.presentation.component.menu_setting_item

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha


@Composable
fun <T> SelectorListItem(
    title: String,
    values: List<T>,
    selection: T,
    formatValue: (T) -> String,
    onSelected: (T) -> Unit,
    enabled: Boolean = values.isNotEmpty()
) {
    var isExpanded by remember { mutableStateOf(false) }
    fun dismiss() {
        isExpanded = false
    }

    ListItem(
        modifier = Modifier
            .clickable(enabled = enabled) {
                isExpanded = true
            },
        headlineContent = {
            Text(
                modifier = Modifier.alpha(enabled = enabled),
                text = title
            )

        },
        trailingContent = {
            Text(
                modifier = Modifier.alpha(enabled = enabled),
                text = title
            )

            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { dismiss() }
            ) {
                for (value in values) {
                    DropdownMenuItem(
                        onClick = {
                            onSelected(value)
                            dismiss()
                        },
                        text = {
                            Text(formatValue(value))
                        }
                    )
                }
            }
        }
    )
}


@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.alpha(
    lowEmphasis: Boolean = false,
    enabled: Boolean = true,
) = composed {
    when {
        !enabled -> alpha(0.33f)
        lowEmphasis -> alpha(0.66f)
        else -> alpha(1f)
    }
}
