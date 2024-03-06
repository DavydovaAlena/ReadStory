package ru.adavydova.booksmart.presentation.component.book_item

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.adavydova.booksmart.R
import ru.adavydova.booksmart.presentation.component.shadow.customShadow

@Composable
fun Modifier.customShadowForTheBook(width: Dp, alpha: Float = 0.1f) = this.customShadow(
    color = MaterialTheme.colorScheme.scrim.copy(alpha = alpha),
    spread = 1.dp,
    blurRadius = 10.dp,
    offsetY = width / 2,
)


@Composable
fun RecentlyOpenedBookImageWithShadow(
    modifier: Modifier = Modifier,
    heightBook: Dp = 145.dp,
    alpha: Float = 0.3f,
    cover: String,
) {
    BookCardWithCustomShadow(
        modifier = modifier,
        alpha = alpha,
        bookCard = {
            RecentlyOpenedBookImage(
                cover = cover,
                heightBook = heightBook
            )
        },
        heightBookCard = 145.dp
    )
}


@Composable
fun RecentlyOpenedBookImage(
    modifier: Modifier = Modifier,
    heightBook: Dp = 145.dp,
    cover: String
) {
    val context = LocalContext.current

    Log.d("Paint", cover)
    AsyncImage(
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .fillMaxWidth()
            .height(heightBook)
            .background(Color.LightGray),
        model = ImageRequest.Builder(context)
            .error(R.drawable.color_logo_with_background)
            .data(cover)
            .build(),
        contentDescription = null
    )


}