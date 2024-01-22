package ru.adavydova.booksmart.data.remote.util

sealed class Resource<T> (val data: T? = null, message: String? = null) {
     class Success<T>(data: T?): Resource<T>(data)
     class Error<T>(val message: String): Resource<T>(data = null, message)
}