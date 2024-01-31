package ru.adavydova.booksmart.presentation.player.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ExpendedPlayerTopBar(
    onCollapseTap: () -> Unit,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)) {

        IconButton(
            onClick = onCollapseTap) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null)
        }
        
    }

}