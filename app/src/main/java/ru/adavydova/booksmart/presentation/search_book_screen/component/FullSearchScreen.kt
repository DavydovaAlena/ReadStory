package ru.adavydova.booksmart.presentation.search_book_screen.component

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.adavydova.booksmart.permission_logic.PermissionTextProvider
import ru.adavydova.booksmart.presentation.search_book_screen.event.SearchBookEvent
import ru.adavydova.booksmart.presentation.search_book_screen.viewmodel.SearchScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFullWindowScreen(
    viewModel: SearchScreenViewModel = hiltViewModel<SearchScreenViewModel>(),
    backClick: () -> Unit,
    checkingThePermission: (PermissionTextProvider, Boolean) -> Unit,
) {
    val context = LocalContext.current
    val searchState by viewModel.searchBooksState.collectAsState()
    val books = searchState.books?.collectAsLazyPagingItems()


    Scaffold(
        topBar = {
            SearchBarForFullWindow(
                query = searchState.query,
                onValueChange = {
                    viewModel.onEvent(SearchBookEvent.UpdateQuery(it))
                    viewModel.onEvent(SearchBookEvent.SearchNews)
                },
                onSearch = {
                    viewModel.onEvent(SearchBookEvent.SearchNews)
                },
                onClick = {

                },
                checkingThePermission = checkingThePermission,
                backClick = backClick

            )
        },

        ) { padding ->

        books?.let { pagingBooks ->

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = padding)
            ) {

                items(pagingBooks.itemCount) {
                    pagingBooks[it]?.let { book ->

                        val url = StringBuilder()
                            .append("https://")
                            .append(
                                book.volumeInfo.imageLinks.substringAfter("//").substringBefore('?')
                            )
                            .append("/images/frontcover/")
                            .append(
                                book.volumeInfo.imageLinks.substringAfter("id=")
                                    .substringBefore('&')
                            )
                            .toString()

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .absolutePadding(
                                    top = padding.calculateBottomPadding(),
                                    right = 10.dp,
                                    bottom = padding.calculateBottomPadding()
                                )
                                .height(70.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {

                            Icon(
                                modifier = Modifier
                                    .size(20.dp)
                                    .weight(1f),
                                imageVector = Icons.Outlined.Search,
                                tint = MaterialTheme.colorScheme.outline,
                                contentDescription = null
                            )

                            Text(
                                modifier = Modifier.weight(4f),
                                maxLines = 2,
                                text = book.volumeInfo.title
                            )

                            Log.d("image", url)
                            AsyncImage(

                                contentScale = ContentScale.Crop,
                                model = ImageRequest.Builder(context)
                                    .data(url)
                                    .build(),
                                contentDescription = null,
                                modifier = Modifier
                                    .weight(1f)
                                    .width(20.dp)
                                    .height(40.dp)
                            )


                        }
                        Spacer(modifier = Modifier.height(4.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .border(1.dp, MaterialTheme.colorScheme.inverseOnSurface)
                        )

                    }
                }

            }
        }


    }

}

