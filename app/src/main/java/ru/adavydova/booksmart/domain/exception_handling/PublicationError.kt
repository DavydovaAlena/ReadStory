package ru.adavydova.booksmart.domain.exception_handling

import org.readium.r2.shared.util.Error
import org.readium.r2.shared.util.asset.AssetRetriever
import org.readium.r2.streamer.PublicationOpener
import ru.adavydova.booksmart.R
import ru.adavydova.booksmart.util.UserError

sealed class PublicationError(
    override val message: String,
    override val cause: Error? = null
) : Error {

    class ReadError(override val cause: org.readium.r2.shared.util.data.ReadError) :
        PublicationError(cause.message, cause.cause)

    class UnsupportedScheme(cause: Error) :
        PublicationError(cause.message, cause.cause)

    class FormatNotSupported(cause: Error) :
        PublicationError(cause.message, cause.cause)

    class InvalidPublication(cause: Error) :
        PublicationError(cause.message, cause.cause)

    class Unexpected(cause: Error) :
        PublicationError(cause.message, cause.cause)

    fun toUserError(): UserError =
        when (this) {
            is InvalidPublication ->
                UserError(R.string.publication_error_invalid_publication, cause = this)

            is Unexpected ->
                UserError(R.string.publication_error_unexpected, cause = this)

            is FormatNotSupported ->
                UserError(R.string.publication_error_unsupported_asset, cause = this)

            is UnsupportedScheme ->
                UserError(R.string.publication_error_scheme_not_supported, cause = this)

            is ReadError ->
                cause.toUserError()
        }

    companion object {

        operator fun invoke(error: AssetRetriever.RetrieveUrlError): PublicationError =
            when (error) {
                is AssetRetriever.RetrieveUrlError.Reading ->
                    ReadError(error.cause)

                is AssetRetriever.RetrieveUrlError.FormatNotSupported ->
                    FormatNotSupported(error)

                is AssetRetriever.RetrieveUrlError.SchemeNotSupported ->
                    UnsupportedScheme(error)
            }

        operator fun invoke(error: AssetRetriever.RetrieveError): PublicationError =
            when (error) {
                is AssetRetriever.RetrieveError.Reading ->
                    ReadError(error.cause)

                is AssetRetriever.RetrieveError.FormatNotSupported ->
                    FormatNotSupported(error)
            }

        operator fun invoke(error: PublicationOpener.OpenError): PublicationError =
            when (error) {
                is PublicationOpener.OpenError.Reading ->
                    ReadError(error.cause)

                is PublicationOpener.OpenError.FormatNotSupported ->
                    FormatNotSupported(error)
            }
    }
}
