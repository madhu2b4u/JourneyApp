package com.divine.journey.di

/**
 * A generic class that holds a value with its loading status.
 *
 * @param T The type of data being held.
 * @property status The current status of the result (LOADING, ERROR, or SUCCESS).
 * @property data The data of the result. May be null.
 * @property message An optional message, typically used for error descriptions.
 */

data class Result<out T>(val status: Status, val data: T?, val message: String?) {

    companion object {

        /**
         * Creates a successful Result.
         *
         * @param data The data to be wrapped in the Result.
         * @return A Result object with SUCCESS status and the provided data.
         */

        fun <T> success(data: T?): Result<T> {
            return Result(Status.SUCCESS, data, null)
        }

        /**
         * Creates an error Result.
         *
         * @param msg The error message.
         * @param data Optional data to be included with the error.
         * @return A Result object with ERROR status, the error message, and optional data.
         */

        fun <T> error(msg: String, data: T? = null): Result<T> {
            return Result(Status.ERROR, data, msg)
        }

        /**
         * Creates a loading Result.
         *
         * @return A Result object with LOADING status and no data or message.
         */

        fun <T> loading(): Result<T> {
            return Result(Status.LOADING, null, null)
        }
    }
}

/**
 * Enum representing the status of a Result.
 */

enum class Status {
    LOADING,
    ERROR,
    SUCCESS
}