package ru.adavydova.booksmart.presentation.player.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ru.adavydova.booksmart.R
import ru.adavydova.booksmart.presentation.component.button.PlayPauseButton
import ru.adavydova.booksmart.presentation.component.timebar.PlayerLinerProgressIndicator
import ru.adavydova.booksmart.service.PlayerState
import ru.adavydova.booksmart.service.isBuffering

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CompatPlayerView(
    modifier: Modifier = Modifier,
    playerState: PlayerState,
) {


    Column(
        modifier = modifier
            .size(81.dp)
            .fillMaxWidth(),
    ) {

        val currentMediaItem = playerState.currentMediaItem
        currentMediaItem?.let { item ->

            Row(
                modifier = Modifier
                    .background(
                        if (isSystemInDarkTheme())
                            MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.5f)
                        else
                            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f))
                    .height(80.dp)
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Image(
                    modifier = Modifier.size(50.dp),
                    bitmap = ImageBitmap.imageResource(id = R.drawable.player_image2),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(4.dp))

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.8f)
                ) {

                    Text(
                        modifier = Modifier.basicMarquee(),
                        maxLines = 1,
                        text = item.mediaMetadata.displayTitle.toString()
                    )
                    Text(
                        color = MaterialTheme.colorScheme.outline,
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = 2,
                        text = item.mediaMetadata.artist.toString()
                    )
                }

                PlayPauseButton(
                    modifier = Modifier
                        .size(40.dp),
                    isPlaying = playerState.isPlaying,
                    isBuffering = playerState.isBuffering
                ) {
                    with(playerState.player) {
                        playWhenReady = !playWhenReady
                    }
                }

            }


            PlayerLinerProgressIndicator(
                modifier = Modifier
                    .height(2.dp)
                    .fillMaxWidth(),
                player = playerState.player
            )

        }
    }
}