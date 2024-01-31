package ru.adavydova.booksmart.data.mapper

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import ru.adavydova.booksmart.data.local.audio.AudioData

fun AudioData.toMediaItem(): MediaItem{
   val metadata = MediaMetadata.Builder()
       .setDisplayTitle(displayName)
       .setArtist(artist)
       .setTitle(title)
       .build()

   return MediaItem.Builder()
       .setUri(uri)
       .setMediaId(id.toString())
       .setMediaMetadata(metadata)
       .build()

}