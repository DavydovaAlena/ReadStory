package ru.adavydova.booksmart

import android.content.Context
import android.view.View
import dagger.hilt.android.qualifiers.ApplicationContext
import org.readium.r2.lcp.LcpError
import org.readium.r2.lcp.LcpService
import org.readium.r2.lcp.auth.LcpDialogAuthentication
import org.readium.r2.shared.util.DebugError
import org.readium.r2.shared.util.Try
import org.readium.r2.shared.util.asset.AssetRetriever
import org.readium.r2.shared.util.downloads.android.AndroidDownloadManager
import org.readium.r2.shared.util.http.DefaultHttpClient
import org.readium.r2.shared.util.http.HttpClient
import org.readium.r2.streamer.PublicationOpener
import org.readium.r2.streamer.parser.DefaultPublicationParser
import javax.inject.Inject

class Readium @Inject constructor(
    @ApplicationContext context: Context,
    val httpClient: HttpClient,
    private val downloadManager: AndroidDownloadManager,
    val assetRetriever: AssetRetriever
    ) {

    val lcpService = LcpService(
        context,
        assetRetriever,
        downloadManager
    )?.let { Try.success(it) }
        ?: Try.failure(LcpError.Unknown(DebugError("liblcp is missing on the classpath")))


    private val lcpDialogAuthentication = LcpDialogAuthentication()

    private val contentProtections = listOfNotNull(
        lcpService.getOrNull()?.contentProtection(lcpDialogAuthentication)
    )

    val publicationOpener = PublicationOpener(
        publicationParser = DefaultPublicationParser(
            context,
            assetRetriever = assetRetriever,
            httpClient = httpClient,
            // Only required if you want to support PDF files using the PDFium adapter.
            pdfFactory = null
        ),
        contentProtections = contentProtections
    )

}
