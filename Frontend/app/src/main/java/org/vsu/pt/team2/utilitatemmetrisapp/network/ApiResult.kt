package org.vsu.pt.team2.utilitatemmetrisapp.network

class ApiResult<T> {
    var data: T? = null
    var errCode: Int? = null

    fun isSuccess() = errCode == null

    fun ifSuccess(func: T.() -> Unit): ApiResult<T> {
        if (isSuccess())
            data?.let(func)
        return this
    }

    fun ifError(func: (errCode: Int) -> Unit): ApiResult<T> {
        if (!isSuccess())
            errCode?.let(func)
        return this
    }

    companion object {
        fun <T> Error(responseCode: Int): ApiResult<T> {
            return ApiResult<T>().apply {
                errCode = responseCode
            }
        }

        fun <T> Success(data: T): ApiResult<T> {
            return ApiResult<T>().apply {
                this.data = data
            }
        }
    }


}