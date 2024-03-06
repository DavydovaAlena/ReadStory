package ru.adavydova.booksmart.domain.usecase.navigation_book_listener

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.URLUtil
import androidx.browser.customtabs.CustomTabsIntent
import ru.adavydova.booksmart.util.tryOrLog
import javax.inject.Inject

class LaunchWebBrowserUseCase @Inject constructor() {
    operator fun invoke(context:Context, uri: Uri){
        var url = uri
        if (url.scheme == null) {
            url = url.buildUpon().scheme("http").build()
        }

        if (!URLUtil.isNetworkUrl(url.toString())) {
            return
        }

        tryOrLog {
            try {
                CustomTabsIntent.Builder()
                    .build()
                    .launchUrl(context, url)
            } catch (e: ActivityNotFoundException) {
                context.startActivity(Intent(Intent.ACTION_VIEW, url))
            }
        }
    }
}