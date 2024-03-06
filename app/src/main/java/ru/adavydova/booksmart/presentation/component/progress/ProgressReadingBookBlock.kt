package ru.adavydova.booksmart.presentation.component.progress

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt


@Composable
fun ProgressReadingBookBlock(
    modifier: Modifier = Modifier,
    maxLinesTitle: Int = 1,
    maxLinesAuthors: Int = 2,
    visibleLinerProgressIndicator: Boolean = true,
    authors: String? = null,
    title: String = "",
    progress: Float? = null,

    ) {

    Column(modifier = modifier.fillMaxWidth()) {

        Text(
            maxLines = maxLinesTitle,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            text = title
        )

        Spacer(modifier = Modifier.height(4.dp))

        if (authors != null) {
            Text(
                style = MaterialTheme.typography.bodyMedium,
                maxLines = maxLinesAuthors,
                text = authors
            )

            Spacer(modifier = Modifier.height(4.dp))
        }


        progress?.let {
            Text(
                style = MaterialTheme.typography.bodyMedium,
                text = "${(progress * 100.0).roundToInt() / 100.0}%"
            )
            Spacer(modifier = Modifier.height(8.dp))


            if (visibleLinerProgressIndicator) {
                LinearProgressIndicator(
                    progress = progress,
                    trackColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                )
            }
        }


    }

}