package ru.adavydova.booksmart.presentation.screens.detail_book_screen.component

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature


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


@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewBook(
    bookName: String,
    backPressed: () -> Unit,
    url: String
) {

    val lifecycleState = LocalLifecycleOwner.current.lifecycle.observerAsState()
    val state = lifecycleState.value


    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->


            WebView(ctx).apply {


                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                if ( WebViewFeature.isFeatureSupported(WebViewFeature.ALGORITHMIC_DARKENING)){
                    WebSettingsCompat.setAlgorithmicDarkeningAllowed(this.settings, true)
                }

                webViewClient = object : WebViewClient() {
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
                    it.onPause()
                }

                Lifecycle.Event.ON_RESUME -> {
                    it.onResume()
                }

                else -> {
                    Unit
                }
            }

        }
    )


}



fun getUrlBookEdition(name: String, url: String) = StringBuilder()
    .append("https://www.google.ru/books/edition/")
    .append(name.replace(" ", "_"))
    .append("/")
    .append(url)
    .append("?gbpv=1")
    .toString()


