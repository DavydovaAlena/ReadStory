package ru.adavydova.booksmart.presentation.screens.home_screen.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue


@OptIn(ExperimentalFoundationApi::class)
fun PagerState.offsetForPage(page: Int) = (currentPage - page)+ currentPageOffsetFraction

@OptIn(ExperimentalFoundationApi::class)
fun PagerState.indicatorOffsetForPage(page: Int) = 1f - offsetForPage(page).coerceIn(-1f, 1f).absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PageIndicator(
    pageSize: Int,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    selectedPage: Int,
    color: Color = MaterialTheme.colorScheme.tertiaryContainer,
) {

    Row(
        modifier = modifier
            .width(32.dp * pageSize)
            .height(38.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pageSize){
            val offset = pagerState.indicatorOffsetForPage(it)
            Box(modifier = Modifier
                .padding(horizontal = 2.dp)
                .weight(.5f + (offset * 3))
                .height(4.dp)
                .clip(CircleShape)
                .background(
                    color = color,
                    shape = CircleShape),
                contentAlignment = Alignment.Center){

            }
        }
    }


}