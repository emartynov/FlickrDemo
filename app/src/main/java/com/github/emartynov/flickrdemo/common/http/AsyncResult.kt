package com.github.emartynov.flickrdemo.common.http

sealed class AsyncResult<T : Any> {

    fun requireSuccessData(): T {
        return when (this) {
            is Success -> this.data
            else -> throw IllegalStateException("AsyncResult was not successful")
        }
    }

    companion object {

        fun <T : Any> success(data: T): AsyncResult<T> = Success(data)

        fun <T : Any> failure(error: Throwable): AsyncResult<T> = Failure(error)
    }
}

data class Success<T : Any>(val data: T) : AsyncResult<T>()

data class Failure<T : Any>(val error: Throwable) : AsyncResult<T>()
