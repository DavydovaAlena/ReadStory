package ru.adavydova.booksmart.presentation.component.button

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.readium.r2.navigator.preferences.EnumPreference
import org.readium.r2.navigator.preferences.clear
import org.readium.r2.shared.ExperimentalReadiumApi
import ru.adavydova.booksmart.presentation.component.menu_setting_item.ItemMenu

@OptIn(ExperimentalReadiumApi::class)
@Composable
fun <T> ButtonGroupItem(
    title: String,
    preference: EnumPreference<T>,
    commit: () -> Unit,
    formatValue: (T) -> String
) {
    ButtonGroupItem(
        title = title,
        options = preference.supportedValues,
        isActive = preference.isEffective,
        activeOption = preference.effectiveValue,
        selectedOption = preference.value,
        formatValue = formatValue,
        onClear = { preference.clear(); commit() }
            .takeIf { preference.value != null },
        onSelectedOptionChanged = { newValue ->
            if (newValue == preference.value) {
                preference.clear()
            } else {
                preference.set(newValue)
            }
            commit()
        }
    )
}

@Composable
fun <T> ButtonGroupItem(
    title: String,
    options: List<T>,
    isActive: Boolean,
    activeOption: T,
    selectedOption: T?,
    formatValue: (T) -> String,
    onClear: (() -> Unit)?,
    onSelectedOptionChanged: (T) -> Unit
) {
    ItemMenu(title, isActive = isActive, onClear = onClear) {
        ToggleButtonGroup(
            options = options,
            activeOption = activeOption,
            selectedOption = selectedOption,
            onSelectOption = { option -> onSelectedOptionChanged(option) }
        ) { option ->
            Text(
                text = formatValue(option),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }

}