package ru.adavydova.booksmart.presentation.inactive_search_book_screen.filters

sealed class OrderBooks(val order: String): FilterTypeBook {
    object NewestOrderType: OrderBooks("newest")
    object RelevanceOrderType: OrderBooks("relevance")



}


fun String.selectOrderType(): OrderBooks{

    return when(this){
        OrderBooks.NewestOrderType.order -> OrderBooks.NewestOrderType
        OrderBooks.RelevanceOrderType.order -> OrderBooks.RelevanceOrderType
        else -> {
            throw IllegalArgumentException("incorrect value")
        }
    }
}

