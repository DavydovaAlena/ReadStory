package ru.adavydova.booksmart.data.mapper

import ru.adavydova.booksmart.data.remote.dto.books.BooksDto
import ru.adavydova.booksmart.data.remote.dto.books.ItemDto
import ru.adavydova.booksmart.domain.model.Book
import ru.adavydova.booksmart.domain.model.Books

fun BooksDto.toBooks(): Books {

    return Books(

        totalResult = totalItems,
        books = items.map { book ->
            val saleInfo = book.saleInfo
            val volumeInfo = book.volumeInfo
            val accessInfo = book.accessInfo

            Book(
                amount = saleInfo?.listPrice?.amount,
                currencyCode = saleInfo?.listPrice?.currencyCode,
                buyLink = saleInfo?.buyLink,
                saleability = saleInfo?.saleability,
                title = volumeInfo.title ?: "",
                authors = volumeInfo.authors?.joinToString(",")?: "",
                publisher = volumeInfo.publisher,
                publishedDate = volumeInfo.publishedDate ?: "",
                description = volumeInfo.description ?: "",
                pageCount = volumeInfo.pageCount ?: 0,
                identifier = volumeInfo.industryIdentifiers?.first()?.identifier,
                categories = volumeInfo.categories?.joinToString(", ")?: "",
                imageLinks = volumeInfo.imageLinks?.thumbnail
                    ?: volumeInfo.imageLinks?.smallThumbnail ?: "",
                language = volumeInfo.language ?: "",
                isAvailableEpub = accessInfo.epub.isAvailable,
                acsTokenLinkEpub = accessInfo.epub.acsTokenLink,
                downloadLinkEpub = accessInfo.epub.downloadLink,
                isAvailablePdf = accessInfo.pdf.isAvailable,
                acsTokenLinkPdf = accessInfo.pdf.acsTokenLink,
                downloadLinkPdf = accessInfo.pdf.downloadLink,
                webReaderLink = accessInfo.webReaderLink ?: "",
                id = book.id
            )
        }
    )
}

fun ItemDto.toBook(): Book {
    return Book(
        amount = saleInfo?.listPrice?.amount,
        currencyCode = saleInfo?.listPrice?.currencyCode,
        buyLink = saleInfo?.buyLink,
        saleability = saleInfo?.saleability,
        title = volumeInfo.title ?: "",
        authors = volumeInfo.authors?.joinToString(", ")?: "",
        publisher = volumeInfo.publisher,
        publishedDate = volumeInfo.publishedDate ?: "",
        description = volumeInfo.description ?: "",
        pageCount = volumeInfo.pageCount ?: 0,
        identifier = volumeInfo.industryIdentifiers?.first()?.identifier,
        categories = volumeInfo.categories?.joinToString(",")?: "",
        imageLinks = volumeInfo.imageLinks?.thumbnail
            ?: volumeInfo.imageLinks?.smallThumbnail ?: "",
        language = volumeInfo.language ?: "",
        isAvailableEpub = accessInfo.epub.isAvailable,
        acsTokenLinkEpub = accessInfo.epub.acsTokenLink,
        downloadLinkEpub = accessInfo.epub.downloadLink,
        isAvailablePdf = accessInfo.pdf.isAvailable,
        acsTokenLinkPdf = accessInfo.pdf.acsTokenLink,
        downloadLinkPdf = accessInfo.pdf.downloadLink,
        webReaderLink = accessInfo.webReaderLink ?: "",
        id = id
    )
}
