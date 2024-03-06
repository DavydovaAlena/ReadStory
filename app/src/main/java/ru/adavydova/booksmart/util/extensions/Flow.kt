package ru.adavydova.booksmart.util.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn

suspend fun <P> Flow<P>.stateInFirst(
    scope: CoroutineScope,sharingStarted: SharingStarted) = stateIn(
        scope, sharingStarted, first()
    )