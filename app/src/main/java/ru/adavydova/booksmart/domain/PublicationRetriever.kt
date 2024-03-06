package ru.adavydova.booksmart.domain

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.readium.r2.lcp.LcpError
import org.readium.r2.lcp.LcpService
import org.readium.r2.lcp.license.model.LicenseDocument
import org.readium.r2.shared.util.AbsoluteUrl
import org.readium.r2.shared.util.asset.AssetRetriever
import org.readium.r2.shared.util.asset.ResourceAsset
import org.readium.r2.shared.util.data.ReadError
import org.readium.r2.shared.util.file.FileSystemError
import org.readium.r2.shared.util.format.LcpLicenseSpecification
import org.readium.r2.shared.util.getOrElse
import ru.adavydova.booksmart.domain.exception_handling.PublicationError
import ru.adavydova.booksmart.domain.repository.DownloadReadiumBookRepository
import ru.adavydova.booksmart.reader.ComputeStorageDir
import ru.adavydova.booksmart.util.extensions.copyToTempFile
import ru.adavydova.booksmart.util.extensions.moveTo
import ru.adavydova.booksmart.util.tryOrNull
import timber.log.Timber
import java.io.File
import java.util.UUID


/**
 * Retrieves a publication from a remote or local source and import it into the bookshelf storage.
 *
 * If the source file is a LCP license document, the protected publication will be downloaded.
 */
class PublicationRetriever(
    private val listener: Listener,
    createLocalPublicationRetriever: (Listener) -> LocalPublicationRetriever,

    ) {

    private val localPublicationRetriever: LocalPublicationRetriever

    interface Listener {
        fun onSuccess(publication: File, coverUrl: AbsoluteUrl?)
        fun onProgressed(progress: Double)
        fun onError(error: ImportError)
    }

    init {
        localPublicationRetriever = createLocalPublicationRetriever(object : Listener {
            override fun onSuccess(publication: File, coverUrl: AbsoluteUrl?) {
                listener.onSuccess(publication, coverUrl)
            }
            override fun onProgressed(progress: Double) {
                listener.onProgressed(progress)
            }
            override fun onError(error: ImportError) {
                listener.onError(error)
            }
        })

    }

    fun retrieveFromStorage(uri: Uri) {
        localPublicationRetriever.retrieve(uri)
    }

}

/**
 * Retrieves a publication from a file (publication or LCP license document) stored on the device.
 */
class LocalPublicationRetriever(
    private val listener: PublicationRetriever.Listener,
    private val context: Context,
    private val storageDir: ComputeStorageDir,
    private val assetRetriever: AssetRetriever,
    createLcpPublicationRetriever: (PublicationRetriever.Listener) -> LcpPublicationRetriever?
) {

    private val file = storageDir.storageDir
    private val lcpPublicationRetriever: LcpPublicationRetriever?

    private val coroutineScope: CoroutineScope =
        MainScope()

    init {
        lcpPublicationRetriever = createLcpPublicationRetriever(LcpListener())
    }

    /**
     * Retrieves the publication from the given local [uri].
     */

    fun retrieve(uri: Uri) {
        coroutineScope.launch {
            val tempFile = uri.copyToTempFile(context, file)
                .getOrElse {
                    listener.onError(
                        ImportError.FileSystem(FileSystemError.IO(it))
                    )
                    return@launch
                }
            retrieveFromStorage(tempFile)
        }
    }

    /**
     * Retrieves the publication stored at the given [tempFile].
     */
    fun retrieve(
        tempFile: File,
        coverUrl: AbsoluteUrl? = null
    ) {
        coroutineScope.launch {
            retrieveFromStorage(tempFile, coverUrl)
        }
    }

    private suspend fun retrieveFromStorage(
        tempFile: File,
        coverUrl: AbsoluteUrl? = null
    ) {
        val sourceAsset = assetRetriever.retrieve(tempFile)
            .getOrElse {
                listener.onError(
                    ImportError.Publication(PublicationError(it))
                )
                return
            }

        if (
            sourceAsset is ResourceAsset &&
            sourceAsset.format.conformsTo(LcpLicenseSpecification)
        ) {
            if (lcpPublicationRetriever == null) {
                listener.onError(ImportError.MissingLcpSupport)
            } else {
                lcpPublicationRetriever.retrieve(sourceAsset, tempFile, coverUrl)
            }
            return
        }

        val fileExtension = sourceAsset.format.fileExtension
        val fileName = "${UUID.randomUUID()}.${fileExtension.value}"
        val libraryFile = File(file, fileName)

        try {
            tempFile.moveTo(libraryFile)
        } catch (e: Exception) {
            tryOrNull { libraryFile.delete() }
            listener.onError(
                ImportError.Publication(
                    PublicationError.ReadError(
                        ReadError.Access(FileSystemError.IO(e))
                    )
                )
            )
            return
        }

        listener.onSuccess(libraryFile, coverUrl)
    }

    private inner class LcpListener : PublicationRetriever.Listener {
        override fun onSuccess(publication: File, coverUrl: AbsoluteUrl?) {
            coroutineScope.launch {
                retrieve(publication, coverUrl)
            }
        }

        override fun onProgressed(progress: Double) {
            listener.onProgressed(progress)
        }

        override fun onError(error: ImportError) {
            listener.onError(error)
        }
    }
}


/**
 * Retrieves a publication from an LCP license document.
 */
class LcpPublicationRetriever(
    private val listener: PublicationRetriever.Listener,
    private val downloadReadiumBookRepository: DownloadReadiumBookRepository,
    private val lcpPublicationRetriever: org.readium.r2.lcp.LcpPublicationRetriever
) {

    private val coroutineScope: CoroutineScope =
        MainScope()

    init {
        coroutineScope.launch {
            for (download in downloadReadiumBookRepository.all()) {
                lcpPublicationRetriever.register(
                    org.readium.r2.lcp.LcpPublicationRetriever.RequestId(download.id),
                    lcpRetrieverListener
                )
            }
        }
    }

    /**
     * Retrieves a publication protected with the given license.
     */
    fun retrieve(
        licenceAsset: ResourceAsset,
        licenceFile: File,
        coverUrl: AbsoluteUrl?
    ) {
        coroutineScope.launch {
            val license = licenceAsset.resource.read()
                .getOrElse {
                    listener.onError(ImportError.Publication(PublicationError.ReadError(it)))
                    return@launch
                }
                .let {
                    LicenseDocument.fromBytes(it)
                        .getOrElse { error ->
                            listener.onError(
                                ImportError.LcpAcquisitionFailed(error)
                            )
                            return@launch
                        }
                }

            tryOrNull { licenceFile.delete() }

            val requestId = lcpPublicationRetriever.retrieve(
                license,
                lcpRetrieverListener
            )

            downloadReadiumBookRepository.insert(requestId.value, coverUrl)
        }
    }

    private val lcpRetrieverListener: LcpRetrieverListener =
        LcpRetrieverListener()

    private inner class LcpRetrieverListener : org.readium.r2.lcp.LcpPublicationRetriever.Listener {
        override fun onAcquisitionCompleted(
            requestId: org.readium.r2.lcp.LcpPublicationRetriever.RequestId,
            acquiredPublication: LcpService.AcquiredPublication
        ) {
            coroutineScope.launch {
                val coverUrl = downloadReadiumBookRepository.getCover(requestId.value)
                downloadReadiumBookRepository.remove(requestId.value)
                listener.onSuccess(acquiredPublication.localFile, coverUrl)
            }
        }

        override fun onAcquisitionProgressed(
            requestId: org.readium.r2.lcp.LcpPublicationRetriever.RequestId,
            downloaded: Long,
            expected: Long?
        ) {
            coroutineScope.launch {
                val progression =
                    expected?.let { downloaded.toDouble() / expected } ?: return@launch
                listener.onProgressed(progression)
            }
        }

        override fun onAcquisitionFailed(
            requestId: org.readium.r2.lcp.LcpPublicationRetriever.RequestId,
            error: LcpError
        ) {
            coroutineScope.launch {
                downloadReadiumBookRepository.remove(requestId.value)
                listener.onError(
                    ImportError.LcpAcquisitionFailed(error)
                )
            }
        }

        override fun onAcquisitionCancelled(requestId: org.readium.r2.lcp.LcpPublicationRetriever.RequestId) {
            coroutineScope.launch {
                Timber.v("Acquisition " + requestId.value + " has been cancelled.")
                downloadReadiumBookRepository.remove(requestId.value)
            }
        }
    }
}
