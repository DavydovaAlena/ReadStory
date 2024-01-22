package ru.adavydova.booksmart.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class Price(
    val amount: Double?,
    val currencyCode: String?
)
