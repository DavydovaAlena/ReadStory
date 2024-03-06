package ru.adavydova.booksmart.domain

import android.content.Context
import android.net.Uri
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.readium.r2.shared.util.AbsoluteUrl
import org.readium.r2.shared.util.DebugError
import org.readium.r2.shared.util.Try
import org.readium.r2.shared.util.asset.AssetRetriever
import org.readium.r2.shared.util.file.FileSystemError
import org.readium.r2.shared.util.getOrElse
import org.readium.r2.shared.util.toUrl
import org.readium.r2.streamer.PublicationOpener
import ru.adavydova.booksmart.Readium
import ru.adavydova.booksmart.domain.exception_handling.PublicationError
import ru.adavydova.booksmart.domain.repository.DownloadReadiumBookRepository
import ru.adavydova.booksmart.domain.usecase.readium_books.ReadiumBookUseCase
import ru.adavydova.booksmart.reader.ComputeStorageDir
import ru.adavydova.booksmart.util.Resource
import ru.adavydova.booksmart.util.extensions.formatPercentage
import java.io.File
import javax.inject.Inject

data class BookshelfState(
    val success: Boolean? = null,
    val error: ImportError? = null
)

class Bookshelf @Inject constructor(
    @ApplicationContext private val context: Context,
    private val readiumBookUseCase: ReadiumBookUseCase,
    private val computeStorageDir: ComputeStorageDir,
    private val coverStorage: CoverStorage,
    private val readium: Readium,
    private val publicationOpener: PublicationOpener,
    private val assetRetriever: AssetRetriever,
    private val downloadReadiumBookRepository: DownloadReadiumBookRepository
) {


    private val _bookshelfState = MutableStateFlow(BookshelfState())
    val bookshelfState = _bookshelfState.asStateFlow()


    val createPublicationRetriever: (PublicationRetriever.Listener) -> PublicationRetriever

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val publicationRetriever: PublicationRetriever

    init {
        createPublicationRetriever = { listener ->
            PublicationRetriever(
                listener = listener,
                createLocalPublicationRetriever = { localListener ->
                    LocalPublicationRetriever(
                        listener = localListener,
                        context = context,
                        storageDir = computeStorageDir,
                        assetRetriever = readium.assetRetriever,
                        createLcpPublicationRetriever = { lcpListener ->
                            readium.lcpService.getOrNull()?.publicationRetriever()
                                ?.let { retriever ->
                                    LcpPublicationRetriever(
                                        listener = lcpListener,
                                        downloadReadiumBookRepository = downloadReadiumBookRepository,
                                        lcpPublicationRetriever = retriever
                                    )
                                }
                        }
                    )

                }
            )
        }
        publicationRetriever = createPublicationRetriever(PublicationRetrieverListener())
    }

    fun importPublicationFromStorage(
        uri: Uri
    ) {
        publicationRetriever.retrieveFromStorage(uri)
    }

    private inner class PublicationRetrieverListener : PublicationRetriever.Listener {

        override fun onSuccess(publication: File, coverUrl: AbsoluteUrl?) {
            coroutineScope.launch {
                val url = publication.toUrl()
                addBookFeedback(url, coverUrl)
            }
        }

        override fun onProgressed(progress: Double) {
            Log.e("d", "Downloaded ${progress.formatPercentage()}")
        }

        override fun onError(error: ImportError) {
            coroutineScope.launch {
                _bookshelfState.update { it.copy(success = false, error = error) }
            }
        }
    }

    private suspend fun addBookFeedback(
        url: AbsoluteUrl,
        coverUrl: AbsoluteUrl? = null
    ) {
        addBook(url, coverUrl)
            .onSuccess {
                _bookshelfState.update { it.copy(success = true, error = null) }
            }
            .onFailure { importError ->
                _bookshelfState.update { it.copy(success = false, error = importError) }
            }
    }

    private suspend fun addBook(
        url: AbsoluteUrl,
        coverUrl: AbsoluteUrl? = null
    ): Try<Unit, ImportError> {

        val asset = assetRetriever.retrieve(url)
            .getOrElse {
                return Try.failure(
                    ImportError.Publication(PublicationError(it))
                )
            }
        publicationOpener.open(
            asset,
            allowUserInteraction = false
        ).onSuccess { publication ->
            val coverFile =
                coverStorage.storeCover(publication, coverUrl)
                    .getOrElse {
                        return Try.failure(
                            ImportError.FileSystem(
                                FileSystemError.IO(it)
                            )
                        )
                    }
            val id = readiumBookUseCase.insertBookUseCase(
                url,
                asset.format.mediaType,
                publication,
                coverFile
            )
            if (id == -1L) {
                coverFile.delete()
                return Try.failure(
                    ImportError.Database(
                        DebugError("Could not insert book into database.")
                    )
                )
            } else {
                when (val result = readiumBookUseCase.insertTimeOpeningBookUseCase(
                    id = id,
                    cover = coverFile.path,
                    title =publication.metadata.title?: url.filename,
                    lastOpeningTime = System.currentTimeMillis(),
                    progression = "0f")) {
                    is Resource.Error -> {
                        return Try.failure(
                            ImportError.Database(
                                DebugError(result.message)
                            )
                        )
                    }

                    is Resource.Success -> {}
                }

            }

        }
            .onFailure {
                Log.e("s", "Cannot open publication: $it.")
                return Try.failure(
                    ImportError.Publication(PublicationError(it))
                )
            }

        return Try.success(Unit)
    }

}


