package ru.adavydova.booksmart.reader

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.util.Properties

class ComputeStorageDir(
    @ApplicationContext val content: Context
) {

    val storageDir: File
        get() = getComputeStorageDir()
    private fun getComputeStorageDir(): File {
        val properties = Properties()
        val inputStream = content.assets.open("configs/config.properties")
        properties.load(inputStream)
        val useExternalFileDir =
            properties.getProperty("useExternalFileDir", "false")!!.toBoolean()
        return File(
            if (useExternalFileDir) {
                content.getExternalFilesDir(null)?.path + "/"
            } else {
                content.filesDir?.path + "/"
            }
        )
    }
}