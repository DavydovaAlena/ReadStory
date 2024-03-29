package ru.adavydova.booksmart.presentation.component.search_bar

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.foundation.text2.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import ru.adavydova.booksmart.R
import ru.adavydova.booksmart.presentation.permission_logic.PermissionTextProvider
import ru.adavydova.booksmart.presentation.screens.search_book_enable_screen.common.colorSearchBar
import ru.adavydova.booksmart.ui.theme.md_theme_dark_surfaceTint
import java.util.Locale


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnActiveSearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onValueChange: (String) -> Unit,
    goOnRequest: (String) -> Unit,
    clearQuery: ()->Unit,
    checkingThePermission: (PermissionTextProvider, Boolean) -> Unit,
    backClick: () -> Unit,
    useGoogleAssistant: (String) -> Unit

) {

    val focusRequester = remember {
        FocusRequester()
    }
    var microState by remember {
        mutableStateOf(true)
    }


    var queryTextField by remember {
        mutableStateOf(
            TextFieldValue(
                text = query,
                selection = when(query.isEmpty()){
                    true -> TextRange.Zero
                    false -> TextRange(query.length)
                }
            )
        )
    }

    val voiceQueryResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(), onResult = {
            if (it.resultCode == Activity.RESULT_OK) {
                val result =
                    it.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) ?: listOf("")
                val queryText = result[0].toString()
                useGoogleAssistant(queryText)
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

    LaunchedEffect(key1 = LocalContext.current, block = {
        delay(50)
        focusRequester.requestFocus()
    })

    LaunchedEffect(key1 = query, block = {
        microState = query.isEmpty()
    })

    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.inverseOnSurface),
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(
            modifier = Modifier
                .width(50.dp),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = Color(md_theme_dark_surfaceTint.toArgb())
            ),
            onClick = backClick
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.icon_orange),
                contentDescription = null
            )
        }


        TextField(
            modifier = modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = queryTextField,
            maxLines = 2,
            onValueChange = {
                queryTextField = it
                Log.d("p", "pppppp")
                onValueChange(it.text) },
            trailingIcon = {
                when (microState) {
                    true -> {
                        IconButton(onClick = {
                            permissionContract.launch(PermissionTextProvider.RecordAudioTextProvider.permissionName)
                        }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_keyboard_voice_24),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }

                    }

                    false -> {
                        IconButton(onClick = {
                            clearQuery()
                            queryTextField = TextFieldValue(text = "", selection = TextRange.Zero)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }

                    }
                }

            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = { goOnRequest(query) }
            ),
            colors = TextFieldDefaults.colorSearchBar
        )

    }


}