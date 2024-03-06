package ru.adavydova.booksmart.presentation.component.book_item

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.adavydova.booksmart.R
import ru.adavydova.booksmart.presentation.component.search_item.short_variant.parseUrlImage




@Composable
fun FavoriteBookWithShadow(
    modifier: Modifier = Modifier,
    heightBook: Dp = 145.dp,
    cover: String,
){
    BookCardWithCustomShadow(
        modifier = modifier,
        bookCard = {
            FavoriteBookImage(
                cover = cover,
                heightBook = heightBook
            )
        },
        heightBookCard = 145.dp
    )
}

@Composable
fun FavoriteBookImage(
    modifier: Modifier = Modifier,
    heightBook: Dp = 145.dp,
    cover: String
) {
    val context = LocalContext.current

    when (cover) {
        "" -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(heightBook)
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.color_logo_with_background),
                    contentDescription = null
                )
            }

        }
        else -> {
            AsyncImage(
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(heightBook)
                    .background(Color.LightGray),
                model = ImageRequest.Builder(context)
                    .data(cover.parseUrlImage())
                    .build(),
                contentDescription = null
            )
        }
    }


}