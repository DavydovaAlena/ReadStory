package ru.adavydova.booksmart.presentation.screens.inactive_search_book_screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.adavydova.booksmart.presentation.screens.inactive_search_book_screen.ShowState
import ru.adavydova.booksmart.presentation.screens.inactive_search_book_screen.filters.FilterBooks
import ru.adavydova.booksmart.presentation.screens.inactive_search_book_screen.filters.FilterTypeBook
import kotlin.reflect.KClass


inline fun <reified T : KClass<FilterTypeBook>> getFilterTypeName(filterTypeBook: T): String {
    return when (filterTypeBook) {
        FilterBooks::class -> "d"
        else -> ""
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFilterWindow(
    expended: (KClass<out FilterTypeBook>) -> Boolean,
    name: (KClass<out FilterTypeBook>) -> String,
    onExpended: (Boolean, KClass<out FilterTypeBook>) -> Unit,
    closeMenu: (ShowState) -> Unit,
    insertFilter: () -> Unit,
    select: (KClass<out FilterTypeBook>) -> String,
    filters: Map<KClass<out FilterTypeBook>, List<String>>,
    onSelect: (String) -> Unit,
) {


    AlertDialog(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp),
        onDismissRequest = { closeMenu(ShowState.Close) }) {

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                text = "Search filters"
            )

            Spacer(modifier = Modifier.height(20.dp))

            filters.entries.forEachIndexed { index, (filterType, listFilters) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        text = name(filterType)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    ExposedDropDownFilterMenu(
                        modifier = Modifier.weight(2f),
                        selectedText = select(filterType),
                        onSelect = { onSelect(it) },
                        onExpended = { onExpended(it, filterType) },
                        listItems = listFilters,
                        expended = expended(filterType)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
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
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    shape = RoundedCornerShape(10),
                    modifier = Modifier
                        .weight(1f),
                    onClick = { closeMenu(ShowState.Close) }) {
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
            modifier = Modifier.menuAnchor(),
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

            )

        ExposedDropdownMenu(
            expanded = expended,
            onDismissRequest = { onExpended(false) }) {

            listItems.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(text = item)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
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

