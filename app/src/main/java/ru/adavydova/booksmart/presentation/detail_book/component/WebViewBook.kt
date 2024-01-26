package ru.adavydova.booksmart.presentation.detail_book.component

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

const val KEY_DOWN = 12324123

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewBook(
    bookName: String,
    backPressed: () -> Unit,
    url: String
) {
    val context = LocalContext.current
    var view: View? by remember { mutableStateOf(null) }
    var lifecycle by remember {
        mutableStateOf(Lifecycle.Event.ON_CREATE)
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycle = event
        }
        lifecycleOwner.lifecycle.addObserver(observer = observer)
        onDispose {
            Log.d("ok", "l")
            lifecycleOwner.lifecycle.removeObserver(observer = observer)
        }
    }


    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->

            WebView(ctx).apply {

                webViewClient = object : WebViewClient() {

                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        Log.d("1", request?.url?.encodedQuery?: "1")
                        Log.d("2", request?.url?.path?: "2")
                        return if (request?.url?.host?.replaceAfterLast(
                                ".",
                                ""
                            ) == "books.google."
                        ) {
                            super.shouldOverrideUrlLoading(view, request)
                        } else {
                            Intent(Intent.ACTION_VIEW, request?.url).apply {
                                context.startActivity(this)
                            }
                            true
                        }
                    }
                }

                settings.javaScriptEnabled = true
                addJavascriptInterface(AndroidJSInterface, "Android")
                loadUrl(getUrlBookEdition(bookName, url))


            }
        },
        update = {
            Log.d("oki", "oki")
        }
      )


}


object AndroidJSInterface {
    @JavascriptInterface
    fun onExitClick() {
        Log.d("close", "close")
    }
}

fun getUrlBookEdition(name: String, url: String) = StringBuilder()
    .append("https://www.google.ru/books/edition/")
    .append(name.replace(" ", "_"))
    .append("/")
    .append(url)
    .append("?gbpv=1")
    .toString()


