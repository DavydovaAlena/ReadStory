package ru.adavydova.booksmart.presentation.screens.reader_screen.component

import android.content.Context
import android.util.Log
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.commitNow
import androidx.fragment.app.findFragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.readium.r2.navigator.ExperimentalDecorator
import org.readium.r2.navigator.epub.EpubNavigatorFragment
import org.readium.r2.navigator.epub.css.FontStyle
import org.readium.r2.navigator.preferences.FontFamily
import org.readium.r2.shared.ExperimentalReadiumApi
import org.readium.r2.shared.publication.Locator
import org.readium.r2.shared.publication.Publication
import org.readium.r2.shared.publication.services.positionsByReadingOrder
import ru.adavydova.booksmart.presentation.screens.detail_book_screen.component.observerAsState
import ru.adavydova.booksmart.presentation.screens.reader_screen.DecorationStyleAnnotationMark
import ru.adavydova.booksmart.presentation.screens.reader_screen.DecorationStylePageNumber
import ru.adavydova.booksmart.presentation.screens.reader_screen.ReaderViewModel
import ru.adavydova.booksmart.presentation.screens.reader_screen.common.EpubNavigationListenerStateImpl
import ru.adavydova.booksmart.presentation.screens.reader_screen.common.annotationMarkTemplate
import ru.adavydova.booksmart.presentation.screens.reader_screen.common.pageNumberTemplate
import ru.adavydova.booksmart.presentation.screens.reader_screen.fragment.EpubReaderFragment
import ru.adavydova.booksmart.reader.ReaderInitData
import ru.adavydova.booksmart.reader.VisualReaderInitData
import kotlin.math.absoluteValue


suspend fun Publication.getCurrentLocator(progress: Float): Locator? =
    withContext(Dispatchers.Default) {
        val positionsByReadingOrder = positionsByReadingOrder()
        val locator = positionsByReadingOrder.find { listLocation ->
            val firstPosition = listLocation.first().locations.totalProgression
            val lastPosition = listLocation.last().locations.totalProgression
            (firstPosition!!..lastPosition!!).contains(progress.toDouble())
        } ?: return@withContext null

        val index = positionsByReadingOrder().indexOf(locator)
        val positionInResource = positionsByReadingOrder()[index]

        if (positionInResource.isEmpty()) return@withContext null
        var resultLocator: Locator? = null

        positionInResource.foldIndexed(null) { index: Int, acc: Float?, locator: Locator ->
            if (acc == null || (progress - (locator.locations.totalProgression?.toFloat()!!)).absoluteValue <= acc) {
                resultLocator = locator
                (progress - (locator.locations.totalProgression?.toFloat()!!)).absoluteValue
            } else {
                acc
            }
        }

        resultLocator
    }


@OptIn(ExperimentalReadiumApi::class, ExperimentalDecorator::class)
fun FragmentManager.setReaderFragmentFactory(
    readerInitData: ReaderInitData,
    context: Context
): FragmentManager {

    val readData = checkNotNull(readerInitData as? VisualReaderInitData.EpubReaderInitData)
    val epubNavigationListener = EpubNavigationListenerStateImpl(context)
    val screenPixelDensity = context.resources.displayMetrics.density


    this.fragmentFactory = readData.navigatorFactory.createFragmentFactory(
        initialLocator = readData.initialLocation,
        initialPreferences = readData.preferencesManager.preferences.value,
        listener = epubNavigationListener.epubListener,
        configuration = EpubNavigatorFragment.Configuration {
            servedAssets = listOf(
                // For the custom font Literata.
                "fonts/.*",
                // Icon for the annotation side mark, see [annotationMarkTemplate].
                "annotation-icon.svg"
            )
            decorationTemplates[DecorationStyleAnnotationMark::class] = annotationMarkTemplate()
            decorationTemplates[DecorationStylePageNumber::class] =
                pageNumberTemplate(screenPixelDensity)

            addFontFamilyDeclaration(FontFamily.LITERATA) {
                addFontFace {
                    addSource("fonts/Literata-VariableFont_opsz,wght.ttf")
                    setFontStyle(FontStyle.NORMAL)
                    // Literata is a variable font family, so we can provide a font weight range.
                    setFontWeight(200..900)
                }
                addFontFace {
                    addSource("fonts/Literata-Italic-VariableFont_opsz,wght.ttf")
                    setFontStyle(FontStyle.ITALIC)
                    setFontWeight(200..900)
                }
            }
        }
    )
    return this
}


@Composable
fun ReadScreen(navController: NavController) {


    val viewModel = hiltViewModel<ReaderViewModel>()
    val readerState = viewModel.readerBookState.collectAsState()

    readerState.value.readerInitData?.let { readerInitData ->
        ReadFragment(readerInitData = (readerInitData as VisualReaderInitData.EpubReaderInitData))
    }
}


@Composable
fun ReadFragment(
    readerInitData: VisualReaderInitData.EpubReaderInitData
) {
    val lifecycleState = LocalLifecycleOwner.current
    val lifecycleObserverEvent = lifecycleState.lifecycle.observerAsState()
    val event = lifecycleObserverEvent.value


    val containerId by remember { mutableIntStateOf(View.generateViewId()) }
    val childContainerId by remember { mutableIntStateOf(View.generateViewId()) }
    val container = remember { mutableStateOf<FragmentContainerView?>(null) }
    val localContext = LocalContext.current

    val localView = LocalView.current
    val parentFragment = remember(localView) {
        try {
            localView.findFragment<Fragment>()
        } catch (e: IllegalStateException) {
            null
        }
    }

    AndroidView(
        factory = { context ->
            val view = FragmentContainerView(context)
                .apply {
                    id = containerId
                }
            val supportFragmentManager = parentFragment?.childFragmentManager
                ?: (localContext as? FragmentActivity)?.supportFragmentManager
            supportFragmentManager?.setReaderFragmentFactory(readerInitData, context)
            container.value = view
            view
        },
        update = {
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    val supportFragmentManager = parentFragment?.childFragmentManager
                        ?: (localContext as? FragmentActivity)?.supportFragmentManager
                    supportFragmentManager?.commit {
                        setReorderingAllowed(true)
                        add(
                            it.id,
                            EpubReaderFragment::class.java,
                            bundleOf("containerId" to it.id),
                            "reader"
                        )
                    }
                }

                else -> {}
            }

        })


    DisposableEffect(localContext, container) {

        val fragmentManager = parentFragment?.childFragmentManager
            ?: (localContext as? FragmentActivity)?.supportFragmentManager

        onDispose {
            fragmentManager?.fragments?.forEach {
                val fragment = fragmentManager.findFragmentById(it.id)
                if (fragment!=null && !fragmentManager.isStateSaved){
                    fragmentManager.commitNow {
                        setReorderingAllowed(true)
                        remove(fragment)
                    }
                }
            }
            Log.d("Fragment from container", fragmentManager?.fragments.toString() )

        }
    }
}