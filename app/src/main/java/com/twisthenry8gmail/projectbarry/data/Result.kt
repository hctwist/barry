package com.twisthenry8gmail.projectbarry.data

sealed class Result<T> {

    class Success<T>(val data: T) : Result<T>()

    class Failure<T> : Result<T>()

    fun <O> map(mapping: (T) -> O): Result<O> {

        return if (this is Success) {

            Success(mapping(data))
        } else {

            Failure()
        }
    }
}

fun <T> Result<T>?.successOrNull() = if (this is Result.Success) data else null

fun <T> success(data: T) = Result.Success(data)

fun <T> failure() = Result.Failure<T>()