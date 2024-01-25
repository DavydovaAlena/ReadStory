package ru.adavydova.booksmart.data.mapper

import ru.adavydova.booksmart.data.remote.dto.books.BooksDto
import ru.adavydova.booksmart.data.remote.dto.books.ItemDto
import ru.adavydova.booksmart.domain.model.AccessInfo
import ru.adavydova.booksmart.domain.model.Book
import ru.adavydova.booksmart.domain.model.BookFormat
import ru.adavydova.booksmart.domain.model.Books
import ru.adavydova.booksmart.domain.model.Dimensions
import ru.adavydova.booksmart.domain.model.Price
import ru.adavydova.booksmart.domain.model.SaleInfo
import ru.adavydova.booksmart.domain.model.VolumeInfo

fun BooksDto.toBooks(): Books {

    return Books(

        totalResult = totalItems,
        books = items.map { book ->
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
                    saleability = saleInfo?.saleability,

                    ),
                volumeInfo = VolumeInfo(
                    title = volumeInfo.title,
                    authors = volumeInfo.authors ?: listOf(),
                    publisher = volumeInfo.publisher,
                    publishedDate = volumeInfo.publishedDate ?: "",
                    description = volumeInfo.description ?: "",
                    pageCount = volumeInfo.pageCount ?: 0,
                    identifier = volumeInfo.industryIdentifiers?.first()?.identifier,
                    categories = volumeInfo.categories ?: emptyList(),
                    imageLinks = volumeInfo.imageLinks?.thumbnail
                        ?: volumeInfo.imageLinks?.smallThumbnail ?: "",
                    language = volumeInfo.language ?: "",
                ),
                accessInfo = AccessInfo(
                    epub = BookFormat(
                        isAvailable = accessInfo.epub.isAvailable,
                        acsTokenLink = accessInfo.epub.acsTokenLink,
                        downloadLink = accessInfo.epub.downloadLink
                    ),
                    pdf = BookFormat(
                        isAvailable = accessInfo.pdf.isAvailable,
                        acsTokenLink = accessInfo.pdf.acsTokenLink,
                        downloadLink = accessInfo.pdf.downloadLink
                    ),
                    webReaderLink = accessInfo.webReaderLink ?: "",
                ),
                id = book.id
            )
        }
    )
}

fun ItemDto.toBook(): Book{
    return Book(
        id = id,
        volumeInfo = VolumeInfo(
            title = volumeInfo.title,
            authors = volumeInfo.authors ?: listOf(),
            publisher = volumeInfo.publisher,
            publishedDate = volumeInfo.publishedDate ?: "",
            description = volumeInfo.description ?: "",
            identifier = volumeInfo.industryIdentifiers?.first()?.identifier,
            pageCount = volumeInfo.pageCount ?: 0,
            categories = volumeInfo.categories ?: emptyList(),
            imageLinks = volumeInfo.imageLinks?.thumbnail
                ?: volumeInfo.imageLinks?.smallThumbnail ?: "",
            language = volumeInfo.language ?: "",
        ),
        accessInfo = AccessInfo(
            epub = BookFormat(
                isAvailable = accessInfo.epub.isAvailable,
                acsTokenLink = accessInfo.epub.acsTokenLink,
                downloadLink = accessInfo.epub.downloadLink
            ),
            pdf = BookFormat(
                isAvailable = accessInfo.pdf.isAvailable,
                acsTokenLink = accessInfo.pdf.acsTokenLink,
                downloadLink = accessInfo.pdf.downloadLink
            ),
            webReaderLink = accessInfo.webReaderLink ?: "",
        ),
        saleInfo = SaleInfo(
            price = Price(
                amount = saleInfo?.listPrice?.amount,
                currencyCode = saleInfo?.listPrice?.currencyCode
            ),
            buyLink = saleInfo?.buyLink,
            saleability = saleInfo?.saleability,

            )
    )
}
