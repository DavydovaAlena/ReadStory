package ru.adavydova.booksmart.util.extensions

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream

suspend fun InputStream.toFile(file: File) {
    withContext(Dispatchers.IO) {
        use { input ->
            file.outputStream().use { input.copyTo(it) }
        }
    }
}
