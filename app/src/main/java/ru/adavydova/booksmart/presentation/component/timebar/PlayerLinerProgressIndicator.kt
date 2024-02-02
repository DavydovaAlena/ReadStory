package ru.adavydova.booksmart.presentation.component.timebar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.time.debounce
import ru.adavydova.booksmart.service.PlayerState


@OptIn(FlowPreview::class)
@Composable
fun PlayerLinerProgressIndicator(
    modifier: Modifier = Modifier,
    player: Player
) {
    var currentPosition by remember { mutableFloatStateOf(player.currentPosition.toFloat()) }
    var duration by remember { mutableFloatStateOf(player.duration.toFloat()) }



    LaunchedEffect(key1 = player.isPlaying, block = {

        duration = player.duration.toFloat()

        while (isActive && (player.isPlaying )){
            currentPosition = (player.currentPosition.toFloat()/player.duration.toFloat())
            delay(200)
        }

    })


    Box( modifier = modifier) {

        LinearProgressIndicator(
            progress = {
                currentPosition
            },
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.height(2.dp).fillMaxWidth(),
        )
    }

}

