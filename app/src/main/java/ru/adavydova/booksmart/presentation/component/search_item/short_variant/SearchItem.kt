package ru.adavydova.booksmart.presentation.component.search_item.short_variant

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.adavydova.booksmart.domain.model.Book

@Composable
fun SearchItem(
    book: Book,
    onClick: (Book)-> Unit,

) {
   val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(book) }
            .height(70.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Icon(
            modifier = Modifier
                .size(20.dp)
                .weight(1f),
            imageVector = Icons.Outlined.Search,
            tint = MaterialTheme.colorScheme.outline,
            contentDescription = null
        )

        Text(
            modifier = Modifier.weight(5f),
            maxLines = 2,
            text = book.volumeInfo.title
        )

        AsyncImage(
            contentScale = ContentScale.Crop,
            model = ImageRequest.Builder(context)
                .data(book.volumeInfo.imageLinks.parseUrlImage())
                .build(),
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .width(20.dp)
                .height(40.dp)
        )
    }
    Spacer(modifier = Modifier.height(4.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .border(1.dp, MaterialTheme.colorScheme.inverseOnSurface)
    )
}

fun String.parseUrlImage(): String = StringBuilder()
    .append("https://")
    .append(
        substringAfter("//").substringBefore('?')
    )
    .append("/images/frontcover/")
    .append(
        substringAfter("id=").substringBefore('&')
    )
    .toString()