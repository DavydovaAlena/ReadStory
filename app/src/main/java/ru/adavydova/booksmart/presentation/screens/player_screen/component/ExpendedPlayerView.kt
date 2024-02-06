package ru.adavydova.booksmart.presentation.screens.player_screen.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.adavydova.booksmart.presentation.component.timebar.SliderMusicBar
import ru.adavydova.booksmart.service.PlayerState


@OptIn(ExperimentalFoundationApi::class)
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
            .padding(10.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {


        ExpendedPlayerTopBar(playerState = playerState,
            onCollapseTap = { onCollapseTap() })

        Spacer(modifier = Modifier.height(20.dp))

        PlayerArtwork(playerState = playerState)

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .basicMarquee(),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineMedium,
            text = playerState.mediaMetadata.title.toString()
        )

        Spacer(modifier = Modifier.height(5.dp))
        Text(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            text = playerState.mediaMetadata.artist.toString())

        Spacer(modifier = Modifier.height(20.dp))

        SliderMusicBar(
            modifier = Modifier
                .padding(10.dp)
                .height(1.dp)
                .fillMaxWidth(),
            playerState = playerState)

        Spacer(modifier = Modifier.height(20.dp))

        PlayerControls(
            playerState = playerState,
            onPrevClick = onPrevClick,
            onNextClick = onNextClick
        )



    }
}