package ru.adavydova.booksmart.service

import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import ru.adavydova.booksmart.data.local.audio.AudioData

internal val Player.currentMediaItems: List<MediaItem> get() {
    return List(mediaItemCount, ::getMediaItemAt)
}

fun Player.updatePlaylist(incoming: List<MediaItem>) {
    Log.d("33333", "updatePlaylist: itemsToAdd: $mediaItemCount")
    val oldMediaIds = currentMediaItems.map { it.mediaId }.toSet()
    val itemsToAdd = incoming.filterNot { item -> item.mediaId in oldMediaIds }
    addMediaItems(itemsToAdd)
    Log.d("33333", "updatePlaylist: itemsToAdd: $mediaItemCount")
}

fun Player.playMediaAt(index: Int) {
    if (currentMediaItemIndex == index)
        return
    seekToDefaultPosition(index)
    playWhenReady = true
    // Recover from any errors that may have happened at previous media positions
    prepare()
}


val PlayerState.isBuffering get() = playbackState == Player.STATE_BUFFERING