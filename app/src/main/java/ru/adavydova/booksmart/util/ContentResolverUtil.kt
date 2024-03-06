package ru.adavydova.booksmart.util

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.adavydova.booksmart.util.extensions.isDownloadsDocument
import ru.adavydova.booksmart.util.extensions.isExternalStorageDocument
import ru.adavydova.booksmart.util.extensions.isMediaDocument
import ru.adavydova.booksmart.util.extensions.toFile
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import java.net.URL

object ContentResolverUtil {

    suspend fun getContentInputStream(context: Context, uri: Uri, publicationFile: File) {
        withContext(Dispatchers.IO) {
            try {
                val path = getRealPath(context, uri)
                if (path != null) {
                    File(path).copyTo(publicationFile)
                } else {
                    val input = URL(uri.toString()).openStream()
                    input.toFile(publicationFile)
                }
            } catch (e: Exception) {
                val input = getInputStream(context, uri)
                input?.let {
                    input.toFile(publicationFile)
                }
            }
        }
    }


    private fun getInputStream(context: Context, uri: Uri): InputStream? {
        return try {
            context.contentResolver.openInputStream(uri)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    private fun getRealPath(context: Context, uri: Uri): String? {

        if (!DocumentsContract.isDocumentUri(context, uri)
            && "content".equals(uri.scheme!!, ignoreCase = true)
        ) return getDataColumn(context, uri, null, null)

        if (!DocumentsContract.isDocumentUri(context, uri)
            && "file".equals(uri.scheme!!, ignoreCase = true)
        ) return uri.path

        if (!DocumentsContract.isDocumentUri(context, uri)) return null

        when {
            uri.isExternalStorageDocument() -> {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return context.getExternalFilesDir(null).toString() + "/" + split[1]
                }
            }

            uri.isDownloadsDocument() -> {
                val id = DocumentsContract.getDocumentId(uri)
                if (!TextUtils.isEmpty(id)) {
                    if (id.startsWith("raw:")) {
                        return id.replaceFirst("raw:".toRegex(), "")
                    }
                    return try {
                        val contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"),
                            java.lang.Long.valueOf(id)
                        )
                        getDataColumn(context, contentUri, null, null)
                    } catch (e: NumberFormatException) {
                        null
                    }
                }
            }

            uri.isMediaDocument() -> {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                when (type) {
                    "image" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    "video" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    "audio" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])

                return getDataColumn(context, contentUri, selection, selectionArgs)

            }

        }

        return null
    }

    private fun getDataColumn(
        context: Context,
        uri: Uri?,
        selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        val column = "_data"
        val projection = arrayOf(column)
        context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            .use { cursor ->
                cursor?.let {
                    if (cursor.moveToFirst()) {
                        val index = cursor.getColumnIndexOrThrow(column)
                        return cursor.getString(index)
                    }
                }
            }
        return null
    }


}