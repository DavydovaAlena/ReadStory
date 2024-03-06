package ru.adavydova.booksmart.domain

import org.readium.r2.lcp.LcpError
import org.readium.r2.shared.util.DebugError
import org.readium.r2.shared.util.Error
import org.readium.r2.shared.util.downloads.DownloadManager
import org.readium.r2.shared.util.file.FileSystemError
import ru.adavydova.booksmart.R
import ru.adavydova.booksmart.domain.exception_handling.PublicationError
import ru.adavydova.booksmart.domain.exception_handling.toUserError
import ru.adavydova.booksmart.util.UserError

sealed class ImportError(
    override val cause: Error?
) : Error {

    override val message: String =
        "Import failed"

    object MissingLcpSupport :
        ImportError(DebugError("Lcp support is missing."))

    class LcpAcquisitionFailed(
        override val cause: LcpError
    ) : ImportError(cause)

    class Publication(
        override val cause: PublicationError
    ) : ImportError(cause)

    class FileSystem(
        override val cause: FileSystemError
    ) : ImportError(cause)

    class DownloadFailed(
        override val cause: DownloadManager.DownloadError
    ) : ImportError(cause)

    class Opds(override val cause: Error) :
        ImportError(cause)

    class Database(override val cause: Error) :
        ImportError(cause)

    fun toUserError(): UserError = when (this) {
        is MissingLcpSupport -> UserError(R.string.missing_lcp_support, cause = this)
        is Database -> UserError(R.string.import_publication_unable_add_pub_database, cause = this)
        is DownloadFailed -> UserError(R.string.import_publication_download_failed, cause = this)
        is LcpAcquisitionFailed -> cause.toUserError()
        is Opds -> UserError(R.string.import_publication_no_acquisition, cause = this)
        is Publication -> cause.toUserError()
        is FileSystem -> cause.toUserError()

    }
}
