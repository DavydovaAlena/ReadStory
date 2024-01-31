package ru.adavydova.booksmart.data.local.audio

import android.net.Uri
import androidx.media3.common.MediaItem

data class AudioData(
    val uri: Uri,
    val displayName:String,
    val id: Long,
    val duration: Int,
    val artist: String,
    val title: String,
    val mediaItem: MediaItem
)