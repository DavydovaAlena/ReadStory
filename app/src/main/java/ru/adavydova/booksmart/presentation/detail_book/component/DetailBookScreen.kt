package ru.adavydova.booksmart.presentation.detail_book.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.adavydova.booksmart.domain.model.Book
import ru.adavydova.booksmart.presentation.component.search_item.short_variant.parseUrlImage
import ru.adavydova.booksmart.presentation.detail_book.DetailBookViewModel

@Composable
fun DetailBookScreen(
    modifier: Modifier = Modifier,
    book: Book
) {

    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val description = book.volumeInfo.description ?: ""
    val maxLines by remember {
        mutableIntStateOf(3)
    }
    var cutText by remember(description) {
        mutableStateOf<String?>(null)
    }
    var expanded by remember { mutableStateOf(false) }


    val textLayoutState = remember { mutableStateOf<TextLayoutResult?>(null) }
    val seeMoreSizeState = remember { mutableStateOf<IntSize?>(null) }
    val seeMoreOffsetState = remember { mutableStateOf<Offset?>(null) }

    val textLayoutResult = textLayoutState.value
    val seeMoreSize = seeMoreSizeState.value
    val seeMoreOffset = seeMoreOffsetState.value

    LaunchedEffect(description, expanded, textLayoutResult, seeMoreSize) {
        val lastLineIndex = maxLines - 1
        if (!expanded && textLayoutResult != null && seeMoreSize != null
            && lastLineIndex + 1 == textLayoutResult.lineCount && textLayoutResult.isLineEllipsized(
                lastLineIndex
            )
        ) {

            var lastCharIndex = textLayoutResult.getLineEnd(lastLineIndex, visibleEnd = true) + 1
            var charRect: Rect
            do {
                lastCharIndex -= 1
                charRect = textLayoutResult.getCursorRect(lastCharIndex)
            } while (
                charRect.left > textLayoutResult.size.width - seeMoreSize.width
            )
            seeMoreOffsetState.value = Offset(
                charRect.left, charRect.bottom - seeMoreSize.height - 1
            )
            cutText = description.substring(startIndex = 0, endIndex = lastCharIndex - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {

        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()

                .background(Color.Transparent),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            AsyncImage(
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .padding()
                    .shadow(10.dp, RoundedCornerShape(5.dp))
                    .clip(RoundedCornerShape(5.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outline)
                    .height(200.dp)
                    .width(130.dp),

                model = ImageRequest.Builder(context)
                    .data(book.volumeInfo.imageLinks?.parseUrlImage()).build(),
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(16.dp))



            Text(
                textAlign = TextAlign.Center,
                modifier = Modifier,
                color = MaterialTheme.colorScheme.secondary,
                text = book.volumeInfo.title,
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 22.sp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                text = "Author: " + book.volumeInfo.authors.joinToString()
            )


            Spacer(modifier = Modifier.height(4.dp))


            Text(
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                text = "Categories: " + (book.volumeInfo.categories)?.joinToString()
            )


            Spacer(modifier = Modifier.height(8.dp))

            Box(
                Modifier
                    .fillMaxWidth(0.6f)
                    .height(1.dp)
                    .border(1.dp, MaterialTheme.colorScheme.primaryContainer)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.fillMaxWidth()) {

                Text(
                    modifier = Modifier,
                    maxLines = if (expanded) Int.MAX_VALUE else maxLines,
                    overflow = TextOverflow.Ellipsis,
                    onTextLayout = {
                        textLayoutState.value = it
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    text = cutText ?: description
                )

                if (!expanded) {
                    val density = LocalDensity.current
                    Text(
                        onTextLayout = { seeMoreSizeState.value = it.size },
                        modifier = Modifier
                            .offsetShowMoreLabel(seeMoreOffset, density)
                            .clickable {
                                expanded = true
                                cutText = null
                            }
                            .alpha(if (seeMoreOffset != null) 1f else 0f),
                        text = "...Show more")
                }


            }

        }


        Spacer(modifier = Modifier.height(8.dp))


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Bottom
        ) {

            Button(
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { /*TODO*/ }) {

                Text(
                    style = MaterialTheme.typography.titleMedium,
                    text = "Buy now"
                )

            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { /*TODO*/ }) {

                Text(
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.background,
                    text = "Read the free fragment"
                )


            }


        }

    }


}


@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.offsetShowMoreLabel(seeMoreOffset: Offset?, density: Density): Modifier = composed {
    Modifier.then(
        if (seeMoreOffset != null) {
            Modifier.offset(
                x = with(density) { seeMoreOffset.x.toDp() },
                y = with(density) { seeMoreOffset.y.toDp() })
        } else {
            Modifier
        }
    )
}
