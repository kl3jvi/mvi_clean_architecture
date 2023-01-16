package com.kl3jvi.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Status {
    @SerialName("open")
    OPEN,

    @SerialName("order ahead")
    ORDER_AHEAD,

    @SerialName("closed")
    CLOSED;

    companion object {
        fun getTypeFromString(status: String): Status? {
            return enumValueOrNull<Status>(status)
        }

        fun getStringFromType(status: String): String {
            return valueOf(status).name
        }

        private inline fun <reified T : Enum<T>> enumValueOrNull(name: String): T? =
            T::class.java.enumConstants?.firstOrNull { it.name == name }
    }
}
