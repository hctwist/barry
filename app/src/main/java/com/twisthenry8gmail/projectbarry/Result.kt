package com.twisthenry8gmail.projectbarry

sealed class Result<out T> {

    object Waiting : Result<Nothing>()

    class Success<T>(val data: T) : Result<T>()

    object Failure : Result<Nothing>()

    inline fun <O> map(transform: (T) -> O): Result<O> {

        return when (this) {

            is Waiting -> Waiting

            is Success -> success(transform(data))

            is Failure -> Failure
        }
    }

    inline fun <O> switchMap(transform: (T) -> Result<O>): Result<O> {

        return when (this) {

            is Waiting -> Waiting

            is Success -> transform(data)

            is Failure -> Failure
        }
    }

    inline fun ifSuccessful(then: (data: T) -> Unit) {

        if (this is Success) then(data)
    }
}

fun <T> Result<T>?.successOrNull() = if (this is Result.Success) data else null

fun waiting() = Result.Waiting

fun <T> success(data: T) = Result.Success(data)

fun failure() = Result.Failure