package ru.adavydova.booksmart.presentation.detail_book.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ru.adavydova.booksmart.R
import ru.adavydova.booksmart.domain.model.Book

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTopBar(
    navigateBack: ()-> Unit,
    bookUrl: String?,
    bookName:String?,
    favoriteState: Boolean,
    deleteOrInsertBook: ()-> Unit,
    read:Boolean?,
    toRead: (Boolean)-> Unit
) {


    TopAppBar(
        modifier = Modifier.padding(horizontal = 10.dp),
        title = { /*TODO*/ },
        navigationIcon = {
            IconButton(onClick = navigateBack ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null)
            }
        },

        actions = {

            IconButton(onClick = deleteOrInsertBook) {
                when(favoriteState){
                    true -> {
                        Icon(imageVector = Icons.Rounded.Favorite,
                            contentDescription = null)
                    }
                    false -> {
                        Icon(imageVector = Icons.Rounded.FavoriteBorder,
                            contentDescription = null)
                    }
                }

            }

            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.round_access_time_24),
                    contentDescription = null)
            }

            Button(onClick = { toRead(true) }) {
                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    text = "Read")
            }
        })



}