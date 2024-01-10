package ru.adavydova.booksmart.presentation.util

sealed class ResultState {
    object Load: ResultState()
    class Success(val data: Any?): ResultState()
    class Error(val message: String): ResultState()
}