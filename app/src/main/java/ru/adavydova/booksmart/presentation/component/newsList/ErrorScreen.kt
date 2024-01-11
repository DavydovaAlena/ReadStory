package ru.adavydova.booksmart.presentation.component.newsList

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import java.net.ConnectException
import java.net.SocketTimeoutException

@Composable
fun ErrorScreen(
    error: LoadState.Error?,
    modifier: Modifier = Modifier
) {

    val errorMessage = when(error?.error){
        is SocketTimeoutException -> {
            "Server Unavailable"
        }
        is ConnectException -> {
            "Internet Unavailable"
        }
        else -> {
            Log.d("error", (error.toString()?: "nooo").toString())
            "Unknown exception "
        }
    }

    Box(modifier = modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.onBackground)
        .padding(10.dp),
    ){

        Row(modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(imageVector = Icons.Outlined.Info,
                contentDescription = "error",
                tint = MaterialTheme.colorScheme.surface)
            Text(
                maxLines = 2,
                color = MaterialTheme.colorScheme.background,
                text = errorMessage)
        }

    }

}
