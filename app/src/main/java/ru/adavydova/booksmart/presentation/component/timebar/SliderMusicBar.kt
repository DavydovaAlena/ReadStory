package ru.adavydova.booksmart.presentation.component.timebar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import ru.adavydova.booksmart.service.PlayerState
import ru.adavydova.booksmart.util.extensions.isBuffering


@Composable
fun SliderMusicBar(
    modifier: Modifier = Modifier,
    playerState: PlayerState
) {
    val player = playerState.player
    var currentPosition by remember { mutableFloatStateOf((player.currentPosition.toFloat() / player.duration.toFloat()) * 100f) }


    LaunchedEffect(
        key1 = playerState.isPlaying,
        playerState.isBuffering,
        playerState.isPlaying,
        block = {
            while (isActive && (playerState.isPlaying)) {
                currentPosition =
                    (player.currentPosition.toFloat() / player.duration.toFloat()) * 100f
                delay(200)
            }
        })


    Box(modifier = modifier.fillMaxWidth()) {
        Slider(
            colors = SliderDefaults.colors(
                activeTickColor = MaterialTheme.colorScheme.onBackground,
                activeTrackColor = MaterialTheme.colorScheme.onBackground,
                thumbColor = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth(),
            value = currentPosition,
            onValueChange = {
                player.seekTo(((it * player.duration) / 100f).toLong())
                currentPosition =
                    (player.currentPosition.toFloat() / player.duration.toFloat()) * 100f
            },
            valueRange = 0f..100f
        )

    }
}


