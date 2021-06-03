package org.vsu.pt.team2.utilitatemmetrisapp.network

sealed class ApiResult<out T> {

    data class Success<T>(val value: T) : ApiResult<T>()

    data class GenericError(
        val code: Int? = null,
        val error: ErrorResponse? = null
    ) : ApiResult<Nothing>()

    object NetworkError : ApiResult<Nothing>()

}