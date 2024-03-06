package ru.adavydova.booksmart.util.extensions

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File


suspend fun File.moveTo(target: File) = withContext(Dispatchers.IO) {
    if (this@moveTo.renameTo(target)) {
        return@withContext
    }
    // renameTo might be unable to move a file from a filesystem to another. Copy instead.
    copyTo(target)
    delete()
}
