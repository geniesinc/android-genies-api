package com.genies.avatar_api_sampleapp.model

import androidx.lifecycle.MutableLiveData

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Resource<out R> {

    object Loading : Resource<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[message=$message]"
            Loading -> "Loading"
        }
    }
}

data class Success<out T>(val data: T) : Resource<T>()
data class Error(val message: String) : Resource<Nothing>()

/**
 * `true` if [Resource] is of type [Success] & holds non-null [Success.data].
 */
val Resource<*>.succeeded
    get() = this is Success && data != null

fun <T> Resource<T>.successOr(fallback: T): T {
    return (this as? Success<T>)?.data ?: fallback
}

val <T> Resource<T>.data: T?
    get() = (this as? Success)?.data

/**
 * Updates value of [liveData] if [Resource] is of type [Success]
 */
inline fun <reified T> Resource<T>.updateOnSuccess(liveData: MutableLiveData<T>) {
    if (this is Success) {
        liveData.value = data
    }
}