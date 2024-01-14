package ru.adavydova.booksmart.presentation.inactive_search_book_screen.component

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.setValue
import androidx.compose.runtime.structuralEqualityPolicy




class MiExitUntilCollapsedState(
    heightRange: IntRange,
    scrollValue: Int = 0
) : MiScrollFlagState(heightRange, scrollValue) {

    override val offset: Float = 0f

    override val height: Float
        get() = (maxHeight.toFloat() - scrollValue).coerceIn(minHeight.toFloat(), maxHeight.toFloat())

    override var scrollValue: Int
        get() = scrollFlagValue
        set(value) {
            scrollFlagValue = value.coerceAtLeast(0)
        }

    companion object {
        val Saver = run {

            val minHeightKey = "MinHeight"
            val maxHeightKey = "MaxHeight"
            val scrollValueKey = "ScrollValue"

            mapSaver(
                save = {
                    mapOf(
                        minHeightKey to it.minHeight,
                        maxHeightKey to it.maxHeight,
                        scrollValueKey to it.scrollValue
                    )
                },
                restore = {
                    MiExitUntilCollapsedState(
                        heightRange = (it[minHeightKey] as Int)..(it[maxHeightKey] as Int),
                        scrollValue = it[scrollValueKey] as Int
                    )
                }
            )
        }
    }
}


interface MiToolbarState {
    val offset: Float
    val height: Float
    val progress: Float
    var scrollValue: Int
}

abstract class MiScrollFlagState(heightRange: IntRange, scrollValue: Int) : MiToolbarState {
    var scrollFlagValue by mutableStateOf(
        value = scrollValue.coerceAtLeast(0),
        policy = structuralEqualityPolicy()
    )
    val rangeDifference = heightRange.last - heightRange.first
    val minHeight = heightRange.first
    val maxHeight = heightRange.last

    init {
        require(heightRange.first >= 0 && heightRange.last >= heightRange.first) {
            "The lowest height value must be >= 0 and the highest height value must be >= the lowest value."
        }
    }

    final override val progress: Float
        get() = 1 - (maxHeight - height) / rangeDifference
}