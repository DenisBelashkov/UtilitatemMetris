package org.vsu.pt.team2.utilitatemmetrisapp.network

import retrofit2.Response

open class ApiWorker {
    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): ApiResult<T> {
        return safeApiResult(call)
    }

    private suspend fun <T : Any> safeApiResult(call: suspend () -> Response<T>): ApiResult<T> {
        val response = call.invoke()

        return if (response.isSuccessful) {
            val body = response.body()

            if (body == null) {
                ApiResult.Error(response.code())
            } else {
                ApiResult.Success(body)
            }
        } else {
            handleError(response.code())
            ApiResult.Error(response.code())
        }
    }

    protected open fun handleError(errorCode: Int) {
        //pass
    }
}