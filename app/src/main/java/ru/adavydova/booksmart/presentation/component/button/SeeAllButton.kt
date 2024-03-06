package ru.adavydova.booksmart.presentation.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SeeAllOrAddNewBookBlock(
    padding: Dp = 32.dp,
    numberOfItems: Int?,
    addEvent: () -> Unit,
    seeAllEvent: () -> Unit,
    blockTitle: String
) {
    Row(
        modifier = Modifier
            .padding(horizontal = padding)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Absolute.SpaceAround
    ) {


        Text(
            modifier = Modifier.weight(5f),
            style = MaterialTheme.typography.titleMedium,
            text = blockTitle
        )

        when (numberOfItems) {
            0 -> {
                AddABookButton(
                    modifier = Modifier.weight(3f),
                ) {
                    addEvent()
                }

            }

            else -> {
                SeeAllButton(
                    modifier = Modifier.weight(2f),
                ) {
                    seeAllEvent()
                }
            }
        }

    }


}

@Composable
fun AddABookButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Button(
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colorScheme.onBackground,
            containerColor = MaterialTheme.colorScheme.background
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
        modifier = modifier,
        onClick = onClick
    ) {

        Text(text = "Add a book")
    }
}

@Composable
fun SeeAllButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Button(
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colorScheme.onBackground,
            containerColor = MaterialTheme.colorScheme.background
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
        modifier = modifier,
        onClick = onClick
    ) {

        Text(text = "See all")
    }
}