package ru.adavydova.booksmart.presentation.player.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.adavydova.booksmart.presentation.component.button.NextButton
import ru.adavydova.booksmart.presentation.component.button.PlayPauseButton
import ru.adavydova.booksmart.presentation.component.button.PreviousButton
import ru.adavydova.booksmart.service.PlayerState
import ru.adavydova.booksmart.service.isBuffering

@Composable
fun PlayerControls(
    playerState: PlayerState,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PreviousButton(
            modifier = Modifier
                .size(64.dp)
                .padding(8.dp),
            iconTint = MaterialTheme.colorScheme.onSurface
        ) {
            onPrevClick()
        }
        PlayPauseButton(
            modifier = Modifier
                .size(64.dp)
                .background(color = MaterialTheme.colorScheme.onSurface, shape = CircleShape)
                .padding(8.dp),
            isPlaying = playerState.isPlaying,
            isBuffering = playerState.isBuffering,
            iconTint = MaterialTheme.colorScheme.surface
        ) {
            with(playerState.player) {
                playWhenReady = !playWhenReady
            }
        }
        NextButton(
            modifier = Modifier
                .size(64.dp)
                .padding(8.dp),
            iconTint = MaterialTheme.colorScheme.onSurface
        ) {
            onNextClick()
        }
    }

}