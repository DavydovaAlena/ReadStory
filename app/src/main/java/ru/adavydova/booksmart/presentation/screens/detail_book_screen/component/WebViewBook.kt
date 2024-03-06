package ru.adavydova.booksmart.presentation.screens.detail_book_screen.component

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.KeyEvent
import android.view.ViewGroup
import android.webkit.DownloadListener
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import ru.adavydova.booksmart.domain.usecase.google_books_remote.DownloadBookUseCase


@Composable
fun Lifecycle.observerAsState(): State<Lifecycle.Event> {
    val state = remember {
        mutableStateOf(Lifecycle.Event.ON_CREATE)
    }
    DisposableEffect(key1 = this) {
        val observer = LifecycleEventObserver { source, event ->
            state.value = event
        }
        this@observerAsState.addObserver(observer)

        onDispose {
            this@observerAsState.removeObserver(observer)
        }
    }
    return state
}


@Composable
fun Lifecycle.observerAsStateInitial(): State<Lifecycle.State> {
    val state = remember {
        mutableStateOf(Lifecycle.State.INITIALIZED)
    }
    DisposableEffect(key1 = this) {
        val observer = LifecycleEventObserver { source, event ->
            state.value = source.lifecycle.currentState
        }
        this@observerAsStateInitial.addObserver(observer)

        onDispose {
            this@observerAsStateInitial.removeObserver(observer)
        }
    }
    return state
}
@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewBook(
    bookName: String,
    backPressed: () -> Unit,
    url: String
) {
    val downloadManager = remember { DownloadBookUseCase() }
    val lifecycleState = LocalLifecycleOwner.current.lifecycle.observerAsState()
    val state = lifecycleState.value
    var webView:WebView? = null
    var backEnabled by remember {
        mutableStateOf(false)
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->


            WebView(ctx).apply {


                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                this.settings.setSupportZoom(false)
                if ( WebViewFeature.isFeatureSupported(WebViewFeature.ALGORITHMIC_DARKENING)){
                    WebSettingsCompat.setAlgorithmicDarkeningAllowed(this.settings, true)
                }
                setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
                   downloadManager(ctx, url, contentDisposition.substringAfter("filename="), mimetype)
                }

                webViewClient = object : WebViewClient() {

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        removeTopBarWebView(view!!)
                    }

                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        backEnabled = view!!.canGoBack()
                    }
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        return if (request?.url?.host?.replaceAfterLast(
                                ".",
                                ""
                            ) == "books.google."
                        ) {
                            super.shouldOverrideUrlLoading(view, request)
                        } else {
                            Intent(Intent.ACTION_VIEW, request?.url).apply {
                                ctx.startActivity(this)
                            }
                            true
                        }
                    }
                }
                settings.javaScriptEnabled = true
                loadUrl(getUrlBookEdition(bookName, url))

            }
        }, update = {

            when (state) {
                Lifecycle.Event.ON_PAUSE -> {
                    webView = null
                    it.onPause()
                }

                Lifecycle.Event.ON_RESUME -> {
                    webView = it
                    it.onResume()
                }

                else -> {
                    Unit
                }
            }

        }
    )

    BackHandler(enabled = backEnabled) {
        webView?.goBack()
    }
}

fun removeTopBarWebView(view:WebView){
    view.loadUrl("javascript:(function() { document.getElementsByClassName('gb-mobile-icon')[0].style.display='none';})()")
}

fun getUrlBookEdition(name: String, url: String) = StringBuilder()
    .append("https://www.google.ru/books/edition/")
    .append(name.replace(" ", "_"))
    .append("/")
    .append(url)
    .append("?gbpv=1")
    .toString()


