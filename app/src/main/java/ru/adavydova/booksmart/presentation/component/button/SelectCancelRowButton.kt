package ru.adavydova.booksmart.presentation.component.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.adavydova.booksmart.presentation.screens.inactive_search_book_screen.ShowState


@Composable
fun SelectCancelRowButton(
    modifier: Modifier = Modifier,
    closeMenu: ()->Unit,
    select: ()-> Unit
) {

    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            shape = RoundedCornerShape(10),
            modifier = Modifier
                .weight(1f),
            onClick = closeMenu) {
            Text(text = "Cancel")
        }
        Spacer(modifier = Modifier.width(4.dp))
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            modifier = Modifier
                .weight(1f),
            shape = RoundedCornerShape(10),
            onClick = select
        ) {
            Text(text = "Select")
        }
    }
}