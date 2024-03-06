package ru.adavydova.booksmart.presentation.screens.reader_screen.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.readium.r2.navigator.DecorableNavigator
import org.readium.r2.navigator.ExperimentalDecorator
import org.readium.r2.navigator.OverflowableNavigator
import org.readium.r2.navigator.VisualNavigator
import org.readium.r2.navigator.epub.EpubNavigatorFragment
import org.readium.r2.navigator.epub.EpubPreferences
import org.readium.r2.navigator.epub.EpubPreferencesEditor
import org.readium.r2.navigator.epub.EpubSettings
import org.readium.r2.navigator.input.InputListener
import org.readium.r2.navigator.input.TapEvent
import org.readium.r2.navigator.util.DirectionalNavigationAdapter
import org.readium.r2.shared.ExperimentalReadiumApi
import org.readium.r2.shared.publication.Publication
import ru.adavydova.booksmart.domain.ImportError
import ru.adavydova.booksmart.presentation.screens.detail_book_screen.component.observerAsState
import ru.adavydova.booksmart.presentation.screens.reader_screen.ReaderEvent
import ru.adavydova.booksmart.presentation.screens.reader_screen.ReaderViewModel
import ru.adavydova.booksmart.presentation.screens.reader_screen.UserPreferences
import ru.adavydova.booksmart.presentation.screens.reader_screen.common.applyPageNumberDecorations
import ru.adavydova.booksmart.presentation.screens.reader_screen.component.TopBarReader
import ru.adavydova.booksmart.presentation.screens.reader_screen.component.UserPreferences
import ru.adavydova.booksmart.presentation.screens.reader_screen.component.getCurrentLocator


class EpubReaderFragment : Fragment() {


    companion object {
        private const val SEARCH_FRAGMENT_TAG = "search"
        const val NAVIGATOR_FRAGMENT_TAG = "navigator"
        private const val IS_SEARCH_VIEW_ICONIFIED = "isSearchViewIconified"
    }


    @OptIn(
        ExperimentalReadiumApi::class, ExperimentalDecorator::class,
        ExperimentalMaterial3Api::class
    )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        return ComposeView(this.requireContext()).apply {

            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val lifecycleState = LocalLifecycleOwner.current
                val lifecycleObserverEvent = lifecycleState.lifecycle.observerAsState()
                val event = lifecycleObserverEvent.value
                val scope = rememberCoroutineScope()

                val childContainerId by remember { mutableIntStateOf(View.generateViewId()) }
                val viewModel = hiltViewModel<ReaderViewModel>()
                val readerState by viewModel.readerBookState.collectAsState()
                val userPreferences = readerState.userPrefrences
                val editor = userPreferences?.editor?.collectAsState()
                val localContext = LocalContext.current

                val publication = readerState.readerInitData?.publication

                var contentColor by remember { mutableStateOf<Color?>(null) }
                var progress by remember { mutableFloatStateOf(0f) }

                val setNavigationState = remember { mutableStateOf(false) }
                var showBottomSheet by rememberSaveable { mutableStateOf(false) }
                var showTopBar by rememberSaveable { mutableStateOf(false) }

                val onBackStackPressDispatcher =
                    LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher


                readerState.readerInitData?.let { readerInitData ->


                    LaunchedEffect(key1 = editor?.value) {
                        if (editor != null) {
                            val backgroundIntColor =
                                (editor.value as EpubPreferencesEditor).backgroundColor.value?.int
                                    ?: (editor.value as EpubPreferencesEditor).backgroundColor.effectiveValue.int

                            val fontIntColor =
                                (editor.value as EpubPreferencesEditor).textColor.value?.int
                                    ?: (editor.value as EpubPreferencesEditor).textColor.effectiveValue.int

                            contentColor = Color(fontIntColor)
                            container?.setBackgroundColor(backgroundIntColor)
                        }
                    }

                    LaunchedEffect(key1 = parentFragmentManager.fragments) {
                        when (parentFragmentManager.findFragmentById(childContainerId) as? EpubNavigatorFragment) {
                            null -> setNavigationState.value = false
                            else -> setNavigationState.value = true
                        }
                    }


                    LaunchedEffect(key1 = setNavigationState.value) {
                        if (setNavigationState.value && event == Lifecycle.Event.ON_RESUME) {
                            val navigator =
                                parentFragmentManager.findFragmentByTag("nav") as EpubNavigatorFragment
                            (readerState.userPrefrences as? UserPreferences<EpubSettings, EpubPreferences>)?.bind(
                                navigator, viewLifecycleOwner
                            )
                            viewLifecycleOwner.lifecycleScope.launch {
                                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                                    publication?.let {
                                        (navigator as? DecorableNavigator)?.applyPageNumberDecorations(readerInitData.publication)
                                    }
                                }
                            }

                            (navigator as OverflowableNavigator).apply {

                                addInputListener(DirectionalNavigationAdapter(
                                    navigator = this))
                            }

                            (navigator as VisualNavigator).apply {
                                addInputListener(object : InputListener {

                                    override fun onTap(event: TapEvent): Boolean {
                                        showTopBar = !showTopBar
                                        return super.onTap(event)
                                    }
                                })
                            }

                            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                                    navigator.currentLocator
                                        .onEach { locator ->
                                            viewModel.onEvent(ReaderEvent.SaveProgression(locator))
                                            progress = locator.locations.totalProgression?.toFloat() ?: 0f

                                        }
                                        .launchIn(this)
                                }
                            }
                        }
                    }



                    Box(modifier = Modifier.fillMaxSize()) {
                        AndroidView(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    start = 5.dp, end = 5.dp, top = 30.dp, bottom = 60.dp
                                ),
                            factory = { context ->
                                val view = FragmentContainerView(context)
                                    .apply {
                                        id = childContainerId
                                    }
                                view
                            },
                            update = {
                                when (event) {
                                    Lifecycle.Event.ON_CREATE -> {
                                        Log.d("START", parentFragmentManager.fragments.toString())
                                        parentFragmentManager.commit {
                                            setReorderingAllowed(true)
                                            replace(
                                                it.id,
                                                EpubNavigatorFragment::class.java,
                                                Bundle(),
                                                "nav"
                                            )
                                        }
                                    }

                                    else -> {}
                                }
                            })


                        if (showTopBar) {
                            TopBarReader(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .fillMaxWidth(),
                                containerColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                initData = readerInitData,
                                backPressed = {
                                    viewModel.onEvent(
                                        ReaderEvent.CloseReader(
                                            time = System.currentTimeMillis(),
                                            bookId = readerInitData.bookId,
                                            progress = progress.toString()
                                        )
                                    )
                                    onBackStackPressDispatcher?.onBackPressed()
                                },
                                onTtsClick = {},
                                onMenuClick = { viewModel.onEvent(ReaderEvent.OpenOutlineRequest) },
                                onSearch = {},
                                onSettingClick = {
                                    viewModel.onEvent(ReaderEvent.OpenCloseSettingBottomSheet)
                                    showBottomSheet = true
                                },
                                saveBookmark = {},
                                contentColor = MaterialTheme.colorScheme.background
                            )
                        }

                        if (!showTopBar) {
                            LinearProgressIndicator(
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = Color.LightGray,
                                modifier = Modifier
                                    .padding(20.dp)
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                                    .height(1.dp),
                                progress = progress,
                            )
                        }

                        if (showBottomSheet) {
                            ModalBottomSheet(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.9f)
                                    .align(Alignment.BottomCenter),
                                onDismissRequest = {
                                    showBottomSheet = false
                                }) {

                                UserPreferences(
                                    editor = editor!!.value,
                                    commit = { userPreferences.commit() },
                                    title = "User Setting"
                                )

                            }
                        }


                        var dragStateProgress by remember { mutableFloatStateOf(progress) }

                        LaunchedEffect(key1 = progress) {
                            dragStateProgress = progress
                        }

                        if (parentFragmentManager.findFragmentByTag("nav") != null && showTopBar) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                shape = RoundedCornerShape(0.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.15f)
                                    .align(Alignment.BottomCenter)
                            ) {

                                Slider(
                                    modifier = Modifier
                                        .padding(30.dp)
                                        .fillMaxWidth()
                                        .height(1.dp),
                                    colors = SliderDefaults.colors(
                                        thumbColor = MaterialTheme.colorScheme.background,
                                        activeTrackColor = MaterialTheme.colorScheme.background,
                                        inactiveTrackColor = MaterialTheme.colorScheme.outline.copy(
                                            alpha = 0.6f
                                        )
                                    ),

                                    value = dragStateProgress,
                                    onValueChangeFinished = {
                                        scope.launch {
                                            readerInitData.publication.getCurrentLocator(dragStateProgress)
                                                ?.let { locator ->
                                                    (parentFragmentManager.findFragmentByTag("nav")
                                                            as? EpubNavigatorFragment)?.let {
                                                        it.go(locator)
                                                    }
                                                }
                                        }
                                    },

                                    onValueChange = {
                                        dragStateProgress = it
                                    })
                            }

                        }

                    }

                }

            }


        }
    }

    override fun onPause() {
        val fragment = parentFragmentManager.findFragmentByTag("nav")
        val fragment2 = parentFragmentManager.findFragmentByTag("reader")

        if (fragment != null) {
            parentFragmentManager.commit() {
                setReorderingAllowed(true)
                remove(fragment)
            }
        }

        if (fragment2 != null) {
            parentFragmentManager.commit() {
                setReorderingAllowed(true)
                remove(fragment2)
            }
        }
        Log.d("PAUSE", parentFragmentManager.fragments.toString())
        super.onPause()
    }

}
