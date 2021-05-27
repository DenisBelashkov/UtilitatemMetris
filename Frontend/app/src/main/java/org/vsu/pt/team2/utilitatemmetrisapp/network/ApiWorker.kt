package org.vsu.pt.team2.utilitatemmetrisapp.network

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

open class ApiWorker {
    suspend fun <T : Any> safeApiCall(
        dispatcher: CoroutineDispatcher,
        call: suspend () -> T
    ): ApiResult<T> {
        return safeApiResult(dispatcher, call)
    }

    suspend fun <T> safeApiResult(
        dispatcher: CoroutineDispatcher,
        apiCall: suspend () -> T
    ): ApiResult<T> {
        return withContext(dispatcher) {
            try {
                ApiResult.Success<T>(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> ApiResult.NetworkError
                    is HttpException -> {
                        val code = throwable.code()
                        val errorResponse = convertErrorBody(throwable)
                        ApiResult.GenericError(code, errorResponse)
                    }
                    else -> {
                        ApiResult.GenericError(null, null)
                    }
                }
            }
        }
    }

    private fun convertErrorBody(throwable: HttpException): ErrorResponse {
        //todo handle throwable into ErrorResponse if needed
        //https://medium.com/@douglas.iacovelli/how-to-handle-errors-with-retrofit-and-coroutines-33e7492a912

        return ErrorResponse()
    }
}