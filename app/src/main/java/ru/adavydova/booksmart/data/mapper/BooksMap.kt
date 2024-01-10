package ru.adavydova.booksmart.data.mapper

import ru.adavydova.booksmart.data.remote.dto.books.BooksDto
import ru.adavydova.booksmart.domain.model.AccessInfo
import ru.adavydova.booksmart.domain.model.Book
import ru.adavydova.booksmart.domain.model.BookFormat
import ru.adavydova.booksmart.domain.model.Books
import ru.adavydova.booksmart.domain.model.Dimensions
import ru.adavydova.booksmart.domain.model.DownloadAccess
import ru.adavydova.booksmart.domain.model.Price
import ru.adavydova.booksmart.domain.model.SaleInfo
import ru.adavydova.booksmart.domain.model.VolumeInfo

fun BooksDto.toBooks(): Books{

    return Books(
        totalResult = totalItems,
        books = items.map { book->
            val saleInfo = book.saleInfo
            val volumeInfo = book.volumeInfo
            val accessInfo = book.accessInfo

            Book(
                saleInfo = SaleInfo(
                    price = Price(
                        amount = saleInfo?.listPrice?.amount,
                        currencyCode = saleInfo?.listPrice?.currencyCode
                    ),
                    buyLink = saleInfo?.buyLink,
                    country = saleInfo?.country,
                    saleability = saleInfo?.saleability,
                    onSaleDate = saleInfo?.onSaleDate

                ),
                volumeInfo = VolumeInfo(
                    title = volumeInfo.title?: "",
                    subtitle = volumeInfo.subtitle?: "",
                    authors = volumeInfo.authors?: emptyList(),
                    publisher = volumeInfo.publisher?: "",
                    publishedDate = volumeInfo.publishedDate?: "",
                    description = volumeInfo.description?: "",
                    pageCount = volumeInfo.pageCount,
                    mainCategory = volumeInfo.mainCategory?: "",
                    categories = volumeInfo.categories?: emptyList(),
                    imageLinks = volumeInfo.imageLinks?.thumbnail?: "",
                    language = volumeInfo.language?: "",
                    previewLink = volumeInfo.previewLink?: "",
                    canonicalVolumeLink = volumeInfo.canonicalVolumeLink?: "",
                    averageRating = volumeInfo.averageRating?: 0.0,
                    dimensions = Dimensions(
                        height = volumeInfo.dimensions?.height?: "",
                        width = volumeInfo.dimensions?.width?: "",
                        thickness = volumeInfo.dimensions?.thickness?: ""
                    ),
                    ratingsCount = volumeInfo.ratingsCount?: 0,
                    infoLink = volumeInfo.infoLink?: ""

                ),
                accessInfo = AccessInfo(
                    epub = BookFormat(
                        isAvailable = accessInfo.epub.isAvailable?: false,
                        acsTokenLink = accessInfo.epub.acsTokenLink?: "",
                        downloadLink = accessInfo.epub.downloadLink?: ""
                    ),
                    pdf = BookFormat(
                        isAvailable = accessInfo.pdf.isAvailable?: false,
                        acsTokenLink = accessInfo.pdf.acsTokenLink?: "",
                        downloadLink = accessInfo.pdf.downloadLink?: ""
                    ),
                    webReaderLink = accessInfo.webReaderLink?: "",
                ),
                etag = book.etag?: "",
                selfLink = book.selfLink?: "",
                id = book.id
            )
        }
    )

}