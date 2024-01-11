package ru.adavydova.booksmart.presentation.component.search_item.short_variant

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.adavydova.booksmart.R
import ru.adavydova.booksmart.ui.theme.BookSmartTheme

@Composable
fun SimmerSearchItem(
    modifier: Modifier = Modifier
) {

    Card(modifier = Modifier
        .fillMaxWidth()){

        Row(modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(
                top = 10.dp,
                start = 16.dp,
                bottom = 10.dp
            )
            .height(50.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)){

            Box(modifier = Modifier
                .size(20.dp)){
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.width(2.dp))

            Box(modifier = Modifier
                .height(40.dp)
                .fillMaxWidth(0.75f)
            ){
                Column {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shimmerEffect(),
                        text = "")

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .shimmerEffect(),
                        text = "")
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            Box(modifier = Modifier
                .size(45.dp)
                .shimmerEffect()
            )


        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .border(1.dp, MaterialTheme.colorScheme.inverseOnSurface)
        )
    }

}


@Preview
@Composable
fun SimmerSearchItemPreview() {
    BookSmartTheme {
        SimmerSearchItem()
    }
}
fun Modifier.shimmerEffect() = composed {
    val transition = rememberInfiniteTransition(label = "")
    val alpha = transition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    ).value
    background(color = colorResource(id = R.color.shimmer).copy(alpha))
}
