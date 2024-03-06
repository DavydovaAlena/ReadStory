package ru.adavydova.booksmart.util.extensions

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import org.readium.r2.shared.util.Try
import ru.adavydova.booksmart.util.ContentResolverUtil
import ru.adavydova.booksmart.util.tryOrNull
import java.io.File
import java.util.UUID


suspend fun Uri.copyToTempFile(context: Context, dir: File): Try<File, Exception> =
    try {
        val filename = UUID.randomUUID().toString()
        val file = File(dir, "$filename.${extension(context)}")
        ContentResolverUtil.getContentInputStream(context, this, file)
        Try.success(file)
    } catch (e: Exception) {
        Try.failure(e)
    }


fun Uri.isExternalStorageDocument(): Boolean {
    return "com.android.externalstorage.documents" == this.authority
}

fun Uri.isDownloadsDocument(): Boolean {
    return "com.android.providers.downloads.documents" == this.authority
}

fun Uri.isMediaDocument(): Boolean {
    return "com.android.providers.media.documents" == this.authority
}


fun ContentResolver.queryProjection(uri: Uri, projection: String): String? =
    tryOrNull<String?> {
        query(uri, arrayOf(projection), null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                return cursor.getString(0)
            }
            return null
        }
    }

fun Uri.extension(context: Context): String? {
    if (scheme == ContentResolver.SCHEME_CONTENT) {
        tryOrNull {
            context.contentResolver.queryProjection(this, MediaStore.MediaColumns.DISPLAY_NAME)
                ?.let { filename ->
                    File(filename).extension
                        .takeUnless { it.isBlank() }
                }
        }
    }

    return path?.let { File(it).extension }
}


