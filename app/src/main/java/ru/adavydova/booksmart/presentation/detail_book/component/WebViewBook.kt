package ru.adavydova.booksmart.presentation.detail_book.component

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

const val KEY_DOWN = 12324123

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewBook(
    bookName: String,
    backPressed: () -> Unit,
    url: String
) {
    val context = LocalContext.current


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
        })


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


