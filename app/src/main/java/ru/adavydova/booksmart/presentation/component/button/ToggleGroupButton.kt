package ru.adavydova.booksmart.presentation.component.button

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.dp


@Composable
fun <T> ToggleButtonGroup(
    options: List<T>,
    activeOption: T?,
    selectedOption: T?,
    onSelectOption: (T) -> Unit,
    enabled: (T) -> Boolean = { true },
    content: @Composable RowScope.(T) -> Unit
) {
    Row {
        for (option in options) {
            ToggleButton(
                enabled = enabled(option),
                active = activeOption == option,
                selected = selectedOption == option,
                onClick = { onSelectOption(option) },
                content = { content(option) }
            )
        }
    }
}

@Composable
fun ToggleButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
    active: Boolean = false,
    selected: Boolean = false,
    content: @Composable RowScope.() -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        content = content,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.onBackground,
            containerColor = when {
                selected ->
                    MaterialTheme.colorScheme.onBackground
                        .copy(alpha = 0.15f)
                        .compositeOver(MaterialTheme.colorScheme.background)

                active ->
                    MaterialTheme.colorScheme.onBackground
                        .copy(alpha = 0.05f)
                        .compositeOver(MaterialTheme.colorScheme.background)

                else -> MaterialTheme.colorScheme.surface
            }
        ),
        elevation =
        if (selected) {
            ButtonDefaults.elevatedButtonElevation(defaultElevation = 2.dp)
        } else {
            null
        }
    )
}