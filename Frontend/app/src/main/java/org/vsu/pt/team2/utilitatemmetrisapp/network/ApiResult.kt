package org.vsu.pt.team2.utilitatemmetrisapp.network

class ApiResult<T> {
    var data: T? = null
    var errCode: Int? = null

    fun success() = errCode == null

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