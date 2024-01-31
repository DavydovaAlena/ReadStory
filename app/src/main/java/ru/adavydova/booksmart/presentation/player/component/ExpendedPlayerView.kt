package ru.adavydova.booksmart.presentation.player.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.adavydova.booksmart.service.PlayerState


@Composable
fun ExpendedPlayerView(
    modifier: Modifier = Modifier,
    playerState: PlayerState,
    onCollapseTap: () -> Unit,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit
) {

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {

        ExpendedPlayerTopBar { onCollapseTap() }
        PlayerArtwork(playerState = playerState)
        PlayerControls(
            playerState = playerState,
            onPrevClick = onPrevClick,
            onNextClick = onNextClick
        )

    }
}