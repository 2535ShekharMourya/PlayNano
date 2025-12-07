package com.zen.videoplayertestapp.core.sealedstate

sealed class Resource<out T> {
    data class Success<out R>(val result: R): Resource<R>()
    data class Failure(val exception: NetworkException): Resource<Nothing>() // data class Failure(val exception: NetworkException): Resource<Nothing>()
    data object Loading: Resource<Nothing>()
}