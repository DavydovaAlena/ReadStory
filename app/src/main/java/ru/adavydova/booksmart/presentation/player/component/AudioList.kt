package ru.adavydova.booksmart.presentation.player.component

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.session.MediaController
import ru.adavydova.booksmart.R
import ru.adavydova.booksmart.data.local.audio.AudioData
import ru.adavydova.booksmart.presentation.component.button.SeeAllButton
import ru.adavydova.booksmart.service.PlayerState

@Composable
fun AudioList(
    onUpdateList: (List<AudioData>) -> Unit,
    modifier: Modifier = Modifier,
    mediaController: MediaController?,
    onAudioClick: (Int) -> Unit,
    viewModel: AudioFromExternalStorageViewModel = hiltViewModel()
) {

    val audioState by viewModel.audioState.collectAsState()

    LaunchedEffect(key1 = audioState.audio, mediaController?.mediaItemCount, block = {
        Log.d("fefe", "fewef")
        onUpdateList(audioState.audio)
    })


    Column(
        modifier = Modifier
            .fillMaxHeight(0.45f)
            .fillMaxWidth()
    ) {


        Row(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {

            Text(
                style = MaterialTheme.typography.titleLarge,
                text = "Your music file"
            )

            SeeAllButton(){

            }
        }

        LazyHorizontalGrid(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalArrangement = Arrangement.Center,
            rows = GridCells.Fixed(3),
            content = {

                itemsIndexed(audioState.audio) { key, audio ->
                    AudioItem(
                        modifier = Modifier.clickable { onAudioClick(key) },
                        item = audio
                    )
                }
            })
    }


}

@Composable
fun AudioItem(
    modifier: Modifier = Modifier,
    item: AudioData,
) {
    Row(
        modifier = modifier
            .height(120.dp)
            .width(350.dp)
            .padding(6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Image(
            modifier = Modifier
                .size(50.dp)
                .weight(1f),
            bitmap = ImageBitmap.imageResource(id = R.drawable.player_image2),
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(4.dp))

        Column(
            modifier = Modifier
                .weight(4f)
                .fillMaxWidth(0.6f)
        ) {

            Text(
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                text = item.title
            )

            Text(
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline,
                maxLines = 2,
                text = item.artist
            )
        }

        IconButton(
            modifier = Modifier.weight(1f),
            onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null
            )
        }
    }
}