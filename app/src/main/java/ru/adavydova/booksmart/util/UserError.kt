package ru.adavydova.booksmart.util

import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import org.joda.time.DateTime
import org.readium.r2.shared.util.Error
import java.text.DateFormat
import java.util.Date

class UserError private constructor(
    val content: Content,
    val cause: Error?
) {

    constructor(
        @StringRes userMessageId: Int,
        vararg args: Any?,
        cause: org.readium.r2.shared.util.Error?
    ) :
            this(Content(userMessageId, *args), cause)

    constructor(
        @PluralsRes userMessageId: Int,
        quantity: Int?,
        vararg args: Any?,
        cause: Error?
    ) : this(Content(userMessageId, quantity, *args), cause)

    constructor(message: String, cause: Error?) : this(Content(message), cause)

    fun getUserMessage(context: Context): String = content.getUserMessage(context)

}

sealed class Content {
    abstract fun getUserMessage(context: Context): String

    class LocalizedString(
        private val userMessageId: Int,
        private val args: Array<out Any?>,
        private val quantity: Int?
    ) : Content() {
        override fun getUserMessage(context: Context): String {
            val args = args.map {
                when (it) {
                    is Date -> DateFormat.getDateInstance().format(it)
                    is DateTime -> DateFormat.getDateInstance().format(it.toDate())
                    else -> args
                }

            }
            return if (quantity != null) {
                context.resources.getQuantityString(
                    userMessageId,
                    quantity,
                    *(args.toTypedArray())
                )
            } else {
                context.getString(userMessageId, *(args.toTypedArray()))
            }
        }
    }

    class Message(private val message: String) : Content() {
        override fun getUserMessage(context: Context): String {
            return message
        }
    }

    companion object {
        operator fun invoke(@StringRes userMessageId: Int, vararg args: Any?): Content {
            return LocalizedString(userMessageId, args, null)
        }

        operator fun invoke(
            @PluralsRes userMessageId: Int,
            quantity: Int?,
            vararg args: Any?
        ): Content =
            LocalizedString(userMessageId, args, quantity)

        operator fun invoke(message: String): Content =
            Message(message)

    }
}