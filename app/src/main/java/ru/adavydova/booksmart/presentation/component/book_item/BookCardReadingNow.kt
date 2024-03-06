package ru.adavydova.booksmart.presentation.component.book_item

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.adavydova.booksmart.R
import ru.adavydova.booksmart.ui.theme.Typography


@Composable
fun BookCardReadingNowWithShadow(
    heightBookCard: Dp = 250.dp,
    alphaShadow: Float = 0.1f,
    modifier: Modifier,
    authors:String? = null,
    title: String,
    cover: String
) {
    BookCardWithCustomShadow(
        modifier = modifier,
        alpha = alphaShadow,
        bookCard = {
            BookCardReadingNow(
                title = title,
                cover = cover,
                authors = authors,
                heightBookCard = heightBookCard
            )
        },
        heightBookCard = heightBookCard
    )
}


@Composable
fun BookCardReadingNow(
    heightBookCard: Dp = 250.dp,
    authors:String? = null,
    title: String,
    cover: String
) {

    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(heightBookCard)
            .padding(16.dp)
            .shadow(20.dp, shape = RoundedCornerShape(20.dp), clip = false)
            .background(Color.Black)
            .padding(4.dp)
            .zIndex(1f)
            .blur(0.2.dp)
            .border(1.dp, Color.LightGray)
            .background(Color.White)
            .padding(horizontal = 2.2.dp)
            .border(1.dp, Color.LightGray)
            .background(Color.White)
            .shadow(1.dp)
            .padding(horizontal = 2.4.dp)
            .border(1.dp, Color.LightGray)
            .background(Color.White)
            .shadow(1.dp)
            .padding(horizontal = 2.6.dp)
            .border(1.dp, Color.LightGray)
            .background(Color.White)


    ) {

        Box(modifier = Modifier
            .fillMaxHeight()
            .align(Alignment.CenterStart)
            .fillMaxWidth(0.5f)
            .offset { IntOffset(2, 7) }
            .zIndex(7f)
            .padding(start = 2.dp)

        ) {

            AsyncImage(
                colorFilter = ColorFilter.tint(blendMode = BlendMode.Softlight, color = Color.Gray),
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .padding(
                        vertical = 20.dp,
                        horizontal = 14.dp
                    )
                    .fillMaxSize(),
                model = ImageRequest.Builder(context)
                    .error(R.drawable.color_logo_with_background)
                    .data(cover)
                    .build(),
                contentDescription = null
            )
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(1f)
                    .align(Alignment.CenterEnd)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = .0f),
                                Color.LightGray.copy(0.5f)
                            )
                        )
                    )

            )
        }


        Column(modifier = Modifier
            .fillMaxHeight()
            .align(Alignment.CenterEnd)
            .fillMaxWidth(0.5f)
            .offset { IntOffset(-2, 0) }
            .zIndex(5f)
            .padding(end = 1.dp)
            .padding(vertical = 1.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.White,
                        Color.LightGray
                    )
                )
            )
        ) {


            Text(
                color = Color.Black.copy(alpha = .7f),
                style = MaterialTheme.typography.bodySmall.copy( fontFamily = FontFamily(Font(R.font.literata_variable_font__opsz))),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 20.dp,
                        horizontal = 14.dp
                    ),
                text = title
            )

            authors?.let {
                Text(
                    color = Color.Black.copy(alpha = .7f),
                    style = MaterialTheme.typography.bodySmall.copy( fontFamily = FontFamily(Font(R.font.literata_variable_font__opsz))),
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .fillMaxWidth()
                        .padding(
                            vertical = 20.dp,
                            horizontal = 14.dp
                        ),
                    text = authors
                )
            }



        }


    }

}