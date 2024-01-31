package ru.adavydova.booksmart.presentation.player.component

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.adavydova.booksmart.service.PlayerState

@Composable
fun PlayerArtwork(
    playerState: PlayerState
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(32.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize(),
            model = playerState.currentMediaItem?.mediaMetadata?.artworkUri,
            contentDescription = null,
        )
    }
}