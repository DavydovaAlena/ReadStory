package ru.adavydova.booksmart.presentation.component.search_item.middle_variant

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.adavydova.booksmart.domain.model.Book
import ru.adavydova.booksmart.presentation.component.search_item.short_variant.parseUrlImage

@Composable
fun CardBookItem(
    book: Book,
    onClick: (Book) -> Unit,

    ) {
    Card(
        modifier = Modifier
            .clickable { onClick(book) }
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(10.dp))
            .height(180.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSecondary,
            contentColor = MaterialTheme.colorScheme.onBackground,
        ),
    ) {

        val context = LocalContext.current

        Row(
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 16.dp
                )
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                Text(
                    maxLines = 2,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    text = book.title,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    maxLines = 2,
                    style = MaterialTheme.typography.labelLarge,
                    text = book.authors,
                    color = MaterialTheme.colorScheme.onSurface
                )

                book.description?.let {

                    Text(
                        maxLines = 3,
                        modifier = Modifier
                            .padding(bottom = 16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        text = it,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

            }

            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(5.dp)
                    .background(MaterialTheme.colorScheme.scrim)
                    .border(1.dp, MaterialTheme.colorScheme.onSurface)
            )

            book.imageLinks?.let {

                AsyncImage(
                    contentScale = ContentScale.FillBounds,
                    model = ImageRequest.Builder(context)
                        .data(it.parseUrlImage())
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.secondary)
                        .weight(1.2f)
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .border(1.dp, MaterialTheme.colorScheme.onBackground)
                )
            }

        }


//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(
//                    horizontal = 8.dp)
//                .height(1.dp)
//                .border(1.dp, MaterialTheme.colorScheme.surfaceVariant)
//
//        )
    }

}



