package ru.adavydova.booksmart.presentation.screens.search_book_enable_screen.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable

val TextFieldDefaults.colorSearchBar: TextFieldColors
    @Composable
    get() =
        TextFieldDefaults.colors(
            focusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedPlaceholderColor = MaterialTheme.colorScheme.outline,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            disabledIndicatorColor = MaterialTheme.colorScheme.onSurface,
            focusedIndicatorColor = MaterialTheme.colorScheme.surface,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.surface,
        )




