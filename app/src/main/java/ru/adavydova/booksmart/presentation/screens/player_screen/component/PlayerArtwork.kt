package ru.adavydova.booksmart.presentation.screens.player_screen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import ru.adavydova.booksmart.R
import ru.adavydova.booksmart.service.PlayerState

@Composable
fun PlayerArtwork(
    playerState: PlayerState
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .fillMaxHeight(0.50f)
            .fillMaxSize(0.9f),
    ) {

        Image(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .fillMaxSize(),
            bitmap = ImageBitmap.imageResource(id = R.drawable.player_image2),
            contentDescription = null )

//        AsyncImage(
//            modifier = Modifier
//                .background(MaterialTheme.colorScheme.surfaceVariant)
//                .fillMaxSize(),
//            model = playerState.currentMediaItem?.mediaMetadata?.artworkUri,
//            contentDescription = null,
//        )

    }
}