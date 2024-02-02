package ru.adavydova.booksmart.presentation.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppBottomBar(
    items: List<BottomNavigationItem>,
    select: Int,
    onItemClick: (String) -> Unit
) {

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {

        items.forEachIndexed { index, bottomNavigationItem ->
            NavigationBarItem(
                selected = select == index,
                onClick = { onItemClick(bottomNavigationItem.route) },
                icon = {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Icon(
                            contentDescription = null,
                            imageVector =
                            if (select == index)
                                bottomNavigationItem.selectedIcon
                            else bottomNavigationItem.unselectedIcon,
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            style = MaterialTheme.typography.bodyMedium,
                            text = bottomNavigationItem.name
                        )
                    }
                })
        }

    }
}