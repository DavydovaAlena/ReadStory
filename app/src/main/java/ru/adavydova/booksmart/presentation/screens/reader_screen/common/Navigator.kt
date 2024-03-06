package ru.adavydova.booksmart.presentation.screens.reader_screen.common

import android.graphics.Color
import android.util.Log
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.compose.ui.unit.dp
import org.readium.r2.navigator.DecorableNavigator
import org.readium.r2.navigator.Decoration
import org.readium.r2.navigator.ExperimentalDecorator
import org.readium.r2.navigator.epub.EpubNavigatorFragment
import org.readium.r2.navigator.html.HtmlDecorationTemplate
import org.readium.r2.navigator.html.toCss
import org.readium.r2.shared.publication.Publication
import org.readium.r2.shared.publication.epub.pageList
import ru.adavydova.booksmart.presentation.screens.reader_screen.DecorationStyleAnnotationMark
import ru.adavydova.booksmart.presentation.screens.reader_screen.DecorationStylePageNumber

@OptIn(ExperimentalDecorator::class)
suspend fun DecorableNavigator.applyPageNumberDecorations(publication: Publication) {
    val decorations = publication.pageList
        .mapIndexedNotNull { index, link ->
            Log.d("Index", index.toString())
            Log.d("Index", link.toString())
            val label = link.title ?: return@mapIndexedNotNull null
            val locator = publication.locatorFromLink(link) ?: return@mapIndexedNotNull null
            Decoration(
                id = "page-$index",
                locator = locator,
                style = DecorationStylePageNumber(label = label)
            )
        }

    applyDecorations(decorations, "pageNumbers")
}

@OptIn(ExperimentalDecorator::class)
fun annotationMarkTemplate(@ColorInt defaultTint: Int = Color.YELLOW): HtmlDecorationTemplate {
    val className = "bookSmart-annotation-mark"
    val iconUrl = checkNotNull(EpubNavigatorFragment.assetUrl("annotation-icon.svg"))
    return HtmlDecorationTemplate(
        layout = HtmlDecorationTemplate.Layout.BOUNDS,
        width = HtmlDecorationTemplate.Width.PAGE,
        element = { decoration ->
            val style = decoration.style as? DecorationStyleAnnotationMark
            val tint = style?.tint ?: defaultTint
            // Using `data-activable=1` prevents the whole decoration container from being
            // clickable. Only the icon will respond to activation events.
            """
            <div><div data-activable="1" class="$className" style="background-color: ${tint.toCss()} !important"/></div>"
            """
        },
        stylesheet = """
            .$className {
                float: left;
                margin-left: 8px;
                width: 30px;
                height: 30px;
                border-radius: 40%;
                background: url('$iconUrl') no-repeat center;
                background-size: auto 50%;
                opacity: 0.8;
            }
            """
    )
}

/**
 * This Decoration Style is used to display the page number labels in the margins, when a book
 * provides a `page-list`. The label is stored in the [DecorationStylePageNumber] itself.
 *
 * See http://kb.daisy.org/publishing/docs/navigation/pagelist.html
 */
@OptIn(ExperimentalDecorator::class)
fun pageNumberTemplate(screenPixelDensity:Float): HtmlDecorationTemplate {
    val className = "bookSmart-page-number"
    val topPadding = (80 * screenPixelDensity)
    val bottomPadding = (60 * screenPixelDensity)
    val startPadding = (5 * screenPixelDensity)
    val endPadding = (5 * screenPixelDensity)

    return HtmlDecorationTemplate(
        layout = HtmlDecorationTemplate.Layout.BOXES,
        width = HtmlDecorationTemplate.Width.BOUNDS,
        element = { decoration ->
            val style = decoration.style as? DecorationStylePageNumber

            // Using `var(--RS__backgroundColor)` is a trick to use the same background color as
            // the Readium theme. If we don't set it directly inline in the HTML, it might be
            // forced transparent by Readium CSS.
            """
            <div><span class="$className" style="background-color: var(--RS__backgroundColor) !important">${style?.label}</span></div>"
            """
        },
        stylesheet = """
            .$className {
                padding-left: ${endPadding}px; 
                padding-right: ${startPadding}px;
                padding-top: ${topPadding}px;
            }
            """
    )
}