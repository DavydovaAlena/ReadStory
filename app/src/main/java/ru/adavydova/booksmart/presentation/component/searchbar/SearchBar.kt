package ru.adavydova.booksmart.presentation.component.searchbar

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import ru.adavydova.booksmart.R
import ru.adavydova.booksmart.presentation.permission_logic.PermissionTextProvider
import ru.adavydova.booksmart.presentation.search_book_screen.common.colorSearchBar
import java.util.Locale


@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onClick: () -> Unit,
    checkingThePermission: (PermissionTextProvider, Boolean) -> Unit,
    onValueChange: (String) -> Unit,
    goOnRequest: () -> Unit,

    ) {

    val voiceQueryResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(), onResult = {
            if (it.resultCode == Activity.RESULT_OK) {
                val result =
                    it.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) ?: listOf("")
                val queryText = result[0].toString()
                onValueChange(queryText)
                goOnRequest()
            }
        })

    val permissionContract = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            checkingThePermission(PermissionTextProvider.RecordAudioTextProvider, isGranted)
            if (isGranted) {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH
                    )
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                    putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Something")
                }
                voiceQueryResultLauncher.launch(intent)
            }
        }
    )

    val interactionSource = remember {
        MutableInteractionSource()
    }
    val isClicked = interactionSource.collectIsPressedAsState().value

    LaunchedEffect(key1 = isClicked) {
        if (isClicked) {
            onClick()
        }
    }

    TextField(
        modifier = modifier
            .fillMaxWidth(),
        value = query,
        maxLines = 1,
        readOnly = true,
        onValueChange = {},
        placeholder = { if (query == "") Text(text = "Search book") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null
            )
        },
        trailingIcon = {
            IconButton(onClick = {
                permissionContract.launch(PermissionTextProvider.RecordAudioTextProvider.permissionName)
            }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_keyboard_voice_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        interactionSource = interactionSource,
        colors = TextFieldDefaults.colorSearchBar
    )
}


