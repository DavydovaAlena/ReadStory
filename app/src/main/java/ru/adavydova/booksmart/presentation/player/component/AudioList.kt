package ru.adavydova.booksmart.presentation.player.component

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
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ru.adavydova.booksmart.R
import ru.adavydova.booksmart.data.local.audio.AudioData

@Composable
fun AudioList(
    onUpdateList: (List<AudioData>) -> Unit,
    modifier: Modifier = Modifier,
    onAudioClick: (Int) -> Unit,
    audioList: List<AudioData>,
    isPlayerSetupUp: Boolean = false
) {

    LaunchedEffect(key1 = audioList, block = {
        onUpdateList(audioList)
    })


    Column(
        modifier = Modifier
        .fillMaxHeight(0.45f)
        .fillMaxWidth()
    ) {

        Text(
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleLarge,
            text = "Your music file")


        LazyHorizontalGrid(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalArrangement = Arrangement.Center,
            rows = GridCells.Fixed(3),
            content = {

                itemsIndexed(audioList) { key, audio ->
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
            .width(350.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Image(
            modifier = Modifier.size(50.dp).weight(1f),
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_launcher_background),
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
                text = item.title)

            Text(
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline,
                maxLines = 2,
                text = item.artist)
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