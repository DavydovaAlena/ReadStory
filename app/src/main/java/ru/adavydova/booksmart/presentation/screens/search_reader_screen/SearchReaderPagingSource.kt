package ru.adavydova.booksmart.presentation.screens.search_reader_screen

import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.readium.r2.shared.ExperimentalReadiumApi
import org.readium.r2.shared.publication.Locator
import org.readium.r2.shared.publication.LocatorCollection
import org.readium.r2.shared.publication.services.search.SearchTry
import org.readium.r2.shared.util.ErrorException
import org.readium.r2.shared.util.getOrThrow
import timber.log.Timber

@OptIn(ExperimentalReadiumApi::class)
class SearchPagingSource(
    private val listener: Listener?
) : PagingSource<Unit, Locator>() {

    interface Listener {
        suspend fun next(): SearchTry<LocatorCollection?>
    }

    override val keyReuseSupported: Boolean get() = true

    override fun getRefreshKey(state: PagingState<Unit, Locator>): Unit? = null

    override suspend fun load(params: LoadParams<Unit>): LoadResult<Unit, Locator> {
        listener ?: return LoadResult.Page(data = emptyList(), prevKey = null, nextKey = null)
        Timber.d("ok")
        return try {
            val page = listener.next()
                .mapFailure { ErrorException(it) }
                .getOrThrow()
            LoadResult.Page(
                data = page?.locators ?: emptyList(),
                prevKey = null,
                nextKey = if (page == null) null else Unit
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
