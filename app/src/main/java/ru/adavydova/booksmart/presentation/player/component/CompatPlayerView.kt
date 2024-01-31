package ru.adavydova.booksmart.presentation.player.component

import android.content.res.Configuration
import android.graphics.Bitmap.Config
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ru.adavydova.booksmart.R
import ru.adavydova.booksmart.presentation.component.button.PlayPauseButton
import ru.adavydova.booksmart.service.PlayerState
import ru.adavydova.booksmart.service.isBuffering

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CompatPlayerView(
    modifier: Modifier = Modifier,
    playerState: PlayerState
) {
    Card(
        modifier = modifier
            .size(80.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(0)
    ) {

        val currentMediaItem = playerState.currentMediaItem
        currentMediaItem?.let { item ->


            Row(
                modifier = Modifier
                    .background(
                        if (isSystemInDarkTheme()) {
                            Color(0xFF16242C)
                        } else {
                            Color(0xFFDEEFF5)
                        }
                    )
                    .height(80.dp)
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Image(
                    modifier = Modifier.size(50.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_launcher_background),
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
                        text = item.mediaMetadata.displayTitle.toString())
                    Text(
                        color = MaterialTheme.colorScheme.outline,
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = 2,
                        text = item.mediaMetadata.artist.toString())
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


        }
    }
}