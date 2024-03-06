package ru.adavydova.booksmart.presentation.screens.reader_screen.common

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.readium.r2.navigator.epub.EpubNavigatorFragment
import org.readium.r2.shared.ExperimentalReadiumApi
import org.readium.r2.shared.publication.Locator
import org.readium.r2.shared.util.AbsoluteUrl
import org.readium.r2.shared.util.Url
import org.readium.r2.shared.util.data.ReadError
import org.readium.r2.shared.util.toUri
import ru.adavydova.booksmart.domain.usecase.navigation_book_listener.LaunchWebBrowserUseCase
import ru.adavydova.booksmart.util.UserError
import timber.log.Timber
import javax.inject.Inject



interface EpubNavigationListener {
    val epubListener: EpubNavigatorFragment.Listener
}

class EpubNavigationListenerStateImpl(
    context: Context
) : EpubNavigationListener {


    private val readerCommand = ReaderCommand(context)

    override val epubListener: EpubNavigatorFragment.Listener
        get() = listener

    val epubListenerState = readerCommand.epubListenerState.asStateFlow()

    private val listener = object : EpubNavigatorFragment.Listener {
        @ExperimentalReadiumApi
        override fun onExternalLinkActivated(url: AbsoluteUrl) {
            readerCommand.event(ReaderCommand.ActivityCommand.OpenExternalLink(url))
        }

        override fun onResourceLoadFailed(href: Url, error: ReadError) {
            super.onResourceLoadFailed(href, error)
        }

        override fun onJumpToLocator(locator: Locator) {
            super.onJumpToLocator(locator)
        }
    }
}

class ReaderCommand (
    private val context: Context,
) {

    @Inject
    lateinit var launchWebBrowserUseCase: LaunchWebBrowserUseCase

    val scope = CoroutineScope(Dispatchers.IO)
    private val _errorState = MutableStateFlow<String?>(null)
    val errorState = _errorState.asStateFlow()

    val epubListenerState = MutableStateFlow<EpubListenerState>(EpubListenerState())



     fun event(activityCommand: ActivityCommand) {
        when (activityCommand) {
            ActivityCommand.OpenDrmManagementRequested -> {
                TODO()
            }
            is ActivityCommand.OpenExternalLink -> {
                launchWebBrowserUseCase(context, activityCommand.url.toUri())
            }

            ActivityCommand.OpenOutlineRequested -> {
                epubListenerState.update { it.copy(outlineState = !it.outlineState) }
            }
            is ActivityCommand.ToastError -> {
                Timber.e(activityCommand.error.getUserMessage(context))
                epubListenerState.update { it.copy(error = activityCommand.error.getUserMessage(context)) }
                scope.launch {
                    delay(4000)
                }
                epubListenerState.update { it.copy(error = null) }
            }
        }
    }

     data class EpubListenerState(
        val error: String? = null,
        val outlineState: Boolean = false,
        )

    sealed class ActivityCommand {
        object OpenOutlineRequested : ActivityCommand()
        object OpenDrmManagementRequested : ActivityCommand()
        class OpenExternalLink(val url: AbsoluteUrl) : ActivityCommand()
        class ToastError(val error: UserError) : ActivityCommand()
    }
}