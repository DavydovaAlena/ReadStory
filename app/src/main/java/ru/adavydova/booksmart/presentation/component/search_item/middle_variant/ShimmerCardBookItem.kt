package ru.adavydova.booksmart.presentation.component.search_item.middle_variant

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.adavydova.booksmart.presentation.component.search_item.short_variant.shimmerEffect
import ru.adavydova.booksmart.ui.theme.BookSmartTheme

@Composable
fun ShimmerCardBookItem(
    modifier: Modifier = Modifier
) {

    Card (modifier = modifier
        .fillMaxWidth()
        .height(200.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
        )) {


        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 16.dp,
                    start = 16.dp,
                    end = 8.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ){

            Column( verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    modifier = Modifier
                        .shimmerEffect()
                        .fillMaxWidth(0.9f),
                    text = "",
                    style = MaterialTheme.typography.titleMedium)

                Text(
                    modifier = Modifier
                        .shimmerEffect()
                        .fillMaxWidth(0.6f),
                    text = "")

            }


            Icon(
                imageVector = Icons.Default.MoreVert ,
                tint = MaterialTheme.colorScheme.outline,
                contentDescription = null )
        }

        Row(modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)) {

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxSize()
                    .weight(4.5f)
                    .shimmerEffect())

            Column (modifier = Modifier.weight(2f),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(10.dp))
                        .weight(1f)
                        .shimmerEffect())
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 8.dp,
                    vertical = 8.dp
                )
                .height(1.dp)
                .border(1.dp, MaterialTheme.colorScheme.surfaceVariant)

        )
    }

}

@Preview
@Composable
fun ShimmerCardBookItemPreview() {
    BookSmartTheme {
        ShimmerCardBookItem()
    }
}