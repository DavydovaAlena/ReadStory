package ru.adavydova.booksmart.presentation.component.menu_setting_item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.readium.r2.navigator.preferences.RangePreference
import org.readium.r2.navigator.preferences.clear
import org.readium.r2.shared.ExperimentalReadiumApi
import ru.adavydova.booksmart.R

/**
 * Component for a [RangePreference] with decrement and increment buttons.
 */
@OptIn(ExperimentalReadiumApi::class)
@Composable
fun <T : Comparable<T>> StepperItem(
    title: String,
    preference: RangePreference<T>,
    commit: () -> Unit
) {
    StepperItem(
        title = title,
        isActive = preference.isEffective,
        value = preference.value ?: preference.effectiveValue,
        formatValue = preference::formatValue,
        onDecrement = { preference.decrement(); commit() },
        onIncrement = { preference.increment(); commit() },
        onClear = { preference.clear(); commit() }
            .takeIf { preference.value != null }
    )
}

/**
 * Component for a [RangePreference] with decrement and increment buttons.
 */
@Composable
private fun <T> StepperItem(
    title: String,
    isActive: Boolean,
    value: T,
    formatValue: (T) -> String,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit,
    onClear: (() -> Unit)?
) {
    ItemMenu(
        title = title,
        isActive = isActive,
        onClear = onClear
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IconButton(
                onClick = onDecrement,
                content = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.baseline_remove_24),
                        contentDescription = "Less"
                    )
                }
            )

            Text(
                text = formatValue(value),
                modifier = Modifier.widthIn(min = 30.dp),
                textAlign = TextAlign.Center
            )

            IconButton(
                onClick = onIncrement,
                content = {
                    Icon(Icons.Default.Add, contentDescription = "More")
                }
            )
        }
    }
}
