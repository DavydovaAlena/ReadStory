package ru.adavydova.booksmart.presentation.component.button

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.media3.ui.R

@Composable
fun PlayPauseButton(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    isBuffering: Boolean,
    iconTint: Color = MaterialTheme.colorScheme.onSurface,
    onTogglePlayPause: () -> Unit
) {

    var lastStatePlaying by remember {
        mutableStateOf(isPlaying)
    }

    if (isBuffering) {
        IconButton(
            modifier = modifier,
            onClick = {
                onTogglePlayPause()
            }
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(fraction = .8f),
                imageVector = when (lastStatePlaying) {
                    true -> ImageVector.vectorResource(id = R.drawable.exo_icon_pause)
                    false -> Icons.Rounded.PlayArrow
                },
                contentDescription = if (isPlaying) "Pause" else "Play",
                tint = iconTint
            )
        }
    } else {
        IconButton(
            modifier = modifier,
            onClick = {
                onTogglePlayPause()
            }
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(fraction = .8f),
                imageVector =
                when (isPlaying) {
                    true -> {
                        lastStatePlaying = true
                        ImageVector.vectorResource(id = R.drawable.exo_icon_pause)
                    }

                    false -> {
                        lastStatePlaying = false
                        Icons.Rounded.PlayArrow
                    }
                },
                contentDescription = if (isPlaying) "Pause" else "Play",
                tint = iconTint
            )
        }
    }
}


@Composable
fun PlayerProgressIndicator(
    modifier: Modifier = Modifier,
    progressTint: Color = MaterialTheme.colorScheme.onSurface
) {
    Box(
        modifier = modifier
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .fillMaxSize(.8f)
                .align(Alignment.Center),
            color = progressTint,
            trackColor = Color.Transparent
        )
    }
}