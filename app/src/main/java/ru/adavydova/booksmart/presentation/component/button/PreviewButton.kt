package ru.adavydova.booksmart.presentation.component.button

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.media3.ui.R

@Composable
fun PreviousButton(
    modifier: Modifier = Modifier,
    iconTint: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.fillMaxSize(fraction = .8f),
            painter = painterResource(R.drawable.exo_icon_previous),
            contentDescription = "Previous",
            tint = iconTint
        )
    }
}