package ru.adavydova.booksmart.presentation.search_book_screen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.adavydova.booksmart.R
import ru.adavydova.booksmart.permission_logic.PermissionTextProvider
import ru.adavydova.booksmart.presentation.search_book_screen.event.SearchBookEvent
import ru.adavydova.booksmart.presentation.search_book_screen.viewmodel.SearchScreenViewModel

@Composable
fun SearchBookScreen(
    modifier: Modifier = Modifier,
    toSearchFullScreen: () -> Unit,
    checkingThePermission: (PermissionTextProvider, Boolean) -> Unit,
    viewModel: SearchScreenViewModel = hiltViewModel<SearchScreenViewModel>(),
) {

    val searchState by viewModel.searchBooksState.collectAsState()


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),

        ) {

        Image(
            modifier = Modifier.width(200.dp),
            imageVector = if (isSystemInDarkTheme()) {
                ImageVector.vectorResource(id = R.drawable.color_logo___no_background)
            } else {
                ImageVector.vectorResource(id = R.drawable.black_logo___no_background)
            },
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(16.dp))

        SearchBar(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSecondary,
                    shape = CircleShape
                )
                .shadow(
                    elevation = 10.dp,
                    shape = CircleShape
                )
                .clip(CircleShape),
            query = searchState.query,
            onClick = toSearchFullScreen,
            onValueChange = { viewModel.onEvent(SearchBookEvent.UpdateQuery(it)) },
            onSearch = { viewModel.onEvent(SearchBookEvent.SearchNews) },
            checkingThePermission = checkingThePermission
        )

    }
}