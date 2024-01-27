package ru.adavydova.booksmart.presentation.inactive_search_book_screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.adavydova.booksmart.presentation.inactive_search_book_screen.ShowState
import ru.adavydova.booksmart.presentation.inactive_search_book_screen.filters.FilterBooks
import ru.adavydova.booksmart.presentation.inactive_search_book_screen.filters.LanguageRestrictFilterBooks
import ru.adavydova.booksmart.presentation.inactive_search_book_screen.filters.OrderBooks
import ru.adavydova.booksmart.presentation.inactive_search_book_screen.filters.selectFilterType
import ru.adavydova.booksmart.presentation.inactive_search_book_screen.filters.selectLanguageRestrict
import ru.adavydova.booksmart.presentation.inactive_search_book_screen.filters.selectOrderType


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFilterWindow(
    expendedFilter: Boolean,
    onExpendedFilter: (Boolean) -> Unit,
    expendedOrder: Boolean,
    onExpendedOrderMenu: (Boolean) -> Unit,
    expendedLanguage: Boolean,
    onExpendedLanguage: (Boolean) -> Unit,
    closeMenu: (ShowState) -> Unit,
    insertFilter: () -> Unit,
    selectFilter: FilterBooks,
    selectLanguage: LanguageRestrictFilterBooks,
    selectOrder: OrderBooks,
    onSelectFilter: (FilterBooks) -> Unit,
    onSelectLanguageType: (LanguageRestrictFilterBooks) -> Unit,
    onSelectOrderType: (OrderBooks) -> Unit
) {


    val orderItems = listOf(
        OrderBooks.NewestOrderType.order,
        OrderBooks.RelevanceOrderType.order
    )
    val listFilterItems = listOf(
        FilterBooks.Ebooks.filter,
        FilterBooks.FreeEbooks.filter,
        FilterBooks.Full.filter,
        FilterBooks.Partial.filter,
        FilterBooks.PaidEbooks.filter
    )
    val languageItems = listOf(
        LanguageRestrictFilterBooks.DeBooks.language,
        LanguageRestrictFilterBooks.EnBooks.language,
        LanguageRestrictFilterBooks.FrBooks.language,
        LanguageRestrictFilterBooks.RuBooks.language,
        LanguageRestrictFilterBooks.AllLanguage.language
    )

    val interactionSource = remember {
        MutableInteractionSource()
    }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                closeMenu(ShowState.Close)
            }
            .background(Color.Black.copy(alpha = 0.5f))
            .fillMaxSize()

    ) {



        Column(
            modifier = Modifier
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {}
                .fillMaxWidth(0.90f)
                .background(MaterialTheme.colorScheme.background)
                .padding(20.dp),

            ) {

            Text(
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                text = "Search filters"
            )

            Spacer(
                modifier = Modifier
                    .height(20.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    text = "Filter by"
                )

                Spacer(modifier = Modifier.width(4.dp))

                ExposedDropDownFilterMenu(
                    modifier = Modifier.weight(2f),
                    selectedText = selectFilter.filter,
                    onSelect = { onSelectFilter(it.selectFilterType()) },
                    onExpended = { onExpendedFilter(it) },
                    listItems = listFilterItems,
                    expended = expendedFilter
                )

            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    text = "Order type"
                )

                Spacer(modifier = Modifier.width(4.dp))

                ExposedDropDownFilterMenu(
                    modifier = Modifier.weight(2f),
                    selectedText = selectOrder.order,
                    onSelect = { onSelectOrderType(it.selectOrderType()) },
                    onExpended = { onExpendedOrderMenu(it) },
                    listItems = orderItems,
                    expended = expendedOrder
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    text = "Language"
                )

                Spacer(modifier = Modifier.width(4.dp))

                ExposedDropDownFilterMenu(
                    modifier = Modifier.weight(2f),
                    selectedText = selectLanguage.language,
                    onSelect = { onSelectLanguageType(it.selectLanguageRestrict()) },
                    onExpended = { onExpendedLanguage(it) },
                    listItems = languageItems,
                    expended = expendedLanguage
                )

            }

            Spacer(modifier = Modifier.height(30.dp))
            Box(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.outline)
            )
            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { closeMenu(ShowState.Close) }) {
                    Text(text = "Cancel")
                }
                Spacer(modifier = Modifier.width(4.dp))
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = insertFilter
                ) {
                    Text(text = "Select")
                }
            }


        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropDownFilterMenu(
    modifier: Modifier = Modifier,
    selectedText: String,
    onSelect: (String) -> Unit,
    onExpended: (Boolean) -> Unit,
    listItems: List<String>,
    expended: Boolean


) {

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expended,
        onExpandedChange = onExpended
    ) {

        TextField(
            readOnly = true,
            value = selectedText,
            onValueChange = onSelect,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expended)
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                disabledContainerColor = MaterialTheme.colorScheme.background,
                disabledPlaceholderColor = MaterialTheme.colorScheme.background,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
                focusedIndicatorColor = MaterialTheme.colorScheme.background

            ),
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expended,
            onDismissRequest = { onExpended(false) }) {

            listItems.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(text = item)
                    },
                    onClick = {
                        onSelect(item)
                        onExpended(false)
                    })
            }
        }


    }


}

@Composable
fun DropdownMenuOrder() {

}

