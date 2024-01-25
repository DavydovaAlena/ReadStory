package ru.adavydova.booksmart.presentation.inactive_search_book_screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.adavydova.booksmart.presentation.inactive_search_book_screen.filters.FilterBooks
import ru.adavydova.booksmart.presentation.inactive_search_book_screen.filters.LanguageRestrictFilterBooks
import ru.adavydova.booksmart.presentation.inactive_search_book_screen.filters.OrderBooks


@Composable
fun SearchFilterWindow(
    resetFilters: () -> Unit,
    openStateFilter: Boolean,
    onOpenFilterMenu: (Boolean) -> Unit,
    openStateOrder: Boolean,
    onOpenOrderMenu: (Boolean) -> Unit,
    openStateLanguage: Boolean,
    onOpenLanguageMenu: (Boolean) -> Unit,
    onClose: (Boolean) -> Unit,
    applyFilters: (OrderBooks?, FilterBooks?, LanguageRestrictFilterBooks?) -> Unit,
) {
    var selectOrderType by rememberSaveable {
        mutableStateOf<OrderBooks?>(OrderBooks.RelevanceOrderType)
    }

    var selectFilterType by rememberSaveable {
        mutableStateOf<FilterBooks?>(FilterBooks.Full)
    }

    var selectLanguageType by rememberSaveable {
        mutableStateOf<LanguageRestrictFilterBooks?>(null)
    }


    val orderItems = listOf<OrderBooks>(
        OrderBooks.NewestOrderType,
        OrderBooks.RelevanceOrderType
    )
    val filterItems = listOf<FilterBooks>(
        FilterBooks.Ebooks,
        FilterBooks.FreeEbooks,
        FilterBooks.Full,
        FilterBooks.Partial,
        FilterBooks.PaidEbooks
    )
    val languageItems = listOf<LanguageRestrictFilterBooks>(
        LanguageRestrictFilterBooks.DeBooks,
        LanguageRestrictFilterBooks.EnBooks,
        LanguageRestrictFilterBooks.FrBooks,
        LanguageRestrictFilterBooks.RuBooks
    )


    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(0.8f)
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Text(text = "Search filters")

        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Text("Sort by")
            DropdownMenu(
                expanded = openStateOrder,
                onDismissRequest = {
                    onOpenOrderMenu(false)
                }) {

                orderItems.forEachIndexed { index, orderBooks ->
                    DropdownMenuItem(
                        text = { orderBooks.order },
                        onClick = { selectOrderType = orderBooks })
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Text("Sort by")
            DropdownMenu(
                expanded = openStateOrder,
                onDismissRequest = {
                    onOpenOrderMenu(false)
                }) {

                filterItems.forEachIndexed { index, filterBooks ->
                    DropdownMenuItem(
                        text = { filterBooks.filter },
                        onClick = { selectFilterType = filterBooks })
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Text("Sort by")
            DropdownMenu(
                expanded = openStateOrder,
                onDismissRequest = {
                    onOpenOrderMenu(false)
                }) {

                languageItems.forEachIndexed { index, languageBooks ->
                    DropdownMenuItem(
                        text = { languageBooks.language },
                        onClick = { selectLanguageType = languageBooks })
                }
            }
        }
    }

}


@Composable
fun DropdownMenuOrder() {

}

