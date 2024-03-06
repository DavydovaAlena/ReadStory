package ru.adavydova.booksmart.presentation.component.menu_setting_item

import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import org.readium.r2.navigator.preferences.Preference
import org.readium.r2.navigator.preferences.clear
import org.readium.r2.navigator.preferences.toggle
import org.readium.r2.shared.ExperimentalReadiumApi


/**
 * Component for a boolean [Preference].
 */
@OptIn(ExperimentalReadiumApi::class)
@Composable
fun SwitchItem(
    title: String,
    preference: Preference<Boolean>,
    commit: () -> Unit
) {
    SwitchItem(
        title = title,
        value = preference.value ?: preference.effectiveValue,
        isActive = preference.isEffective,
        onCheckedChange = { preference.set(it); commit() },
        onToggle = { preference.toggle(); commit() },
        onClear = { preference.clear(); commit() }
            .takeIf { preference.value != null }
    )
}

/**
 * Switch
 */
@Composable
private fun SwitchItem(
    title: String,
    value: Boolean,
    isActive: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onToggle: () -> Unit,
    onClear: (() -> Unit)?
) {
    ItemMenu(
        title = title,
        isActive = isActive,
        onClick = onToggle,
        onClear = onClear
    ) {
        Switch(
            checked = value,
            onCheckedChange = onCheckedChange
        )
    }
}