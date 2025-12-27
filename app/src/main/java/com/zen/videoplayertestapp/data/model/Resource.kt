package com.zen.videoplayertestapp.data.model

sealed class Resource2<out T> {
    data class Loading<out T>(
        val partialData: T? = null,
        val isLoadingInitial: Boolean = true,
        val isLoadingMore: Boolean = false
    ) : Resource2<T>()

    data class Success<out T>(
        val data: T,
        val message: String? = null,
        val fromCache: Boolean = false,
        val isPaginated: Boolean = false
    ) : Resource2<T>()

    data class Error<out T>(
        val message: String,
        val errorType: ErrorType,
        val throwable: Throwable? = null,
        val partialData: T? = null, // For showing cached data with error
        val shouldRetry: Boolean = true
    ) : Resource2<T>()

    object Empty : Resource2<Nothing>()
}

enum class ErrorType {
    NETWORK,        // No internet, timeout
    SERVER,         // 4xx, 5xx errors
    UNAUTHORIZED,   // 401
    NOT_FOUND,      // 404
    VALIDATION,     // 422
    UNKNOWN         // Other errors
}