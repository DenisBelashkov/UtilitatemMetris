package org.vsu.pt.team2.utilitatemmetrisapp.managers

import com.orhanobut.logger.Logger
import kotlinx.coroutines.delay
import okhttp3.internal.wait
import org.vsu.pt.team2.utilitatemmetrisapp.api.model.SuccessfulLoginUser
import org.vsu.pt.team2.utilitatemmetrisapp.models.User
import org.vsu.pt.team2.utilitatemmetrisapp.network.ApiResult
import javax.inject.Inject

class AuthManager @Inject constructor(
    val sessionManager: SessionManager
) {

    suspend fun authUser(email: String, pass: String): ApiResult<SuccessfulLoginUser> {
        Logger.d("Normal auth. Got email $email, pass $pass")
        delay(300L)
        val result : ApiResult<SuccessfulLoginUser> = ApiResult.Success<SuccessfulLoginUser>(
            SuccessfulLoginUser(
                "jwt_offline_demo"
            )
        )
        when (result) {
            is ApiResult.NetworkError -> {
                /*showtoast internet lost*/
            }
            is ApiResult.GenericError -> {
                sessionManager.clear()
            }
            is ApiResult.Success -> {
                result.value.apply {
                    sessionManager.setSession(User(email, this.token), false)
                }

            }
        }
        return result
    }

    suspend fun authUser(email: String): ApiResult<SuccessfulLoginUser> {
        Logger.d("Demo auth. Got email $email")
        delay(300L)
        val result : ApiResult<SuccessfulLoginUser> = ApiResult.Success<SuccessfulLoginUser>(
            SuccessfulLoginUser(
                "jwt_offline_demo"
            )
        )
        when (result) {
            is ApiResult.NetworkError -> {
                /*showtoast internet lost*/
            }
            is ApiResult.GenericError -> {
                sessionManager.clear()
            }
            is ApiResult.Success -> {
                result.value.apply {
                    sessionManager.setSession(User(email, this.token), true)
                }

            }
        }
        return result
    }

    suspend fun registerUser(email: String, pass: String): ApiResult<*> {
        Logger.d("Register user. Got email $email, pass $pass")
        delay(300L)
        val result : ApiResult<*> = ApiResult.Success(Any())
        when (result) {
            is ApiResult.NetworkError -> {
                /*showtoast internet lost*/
            }
            is ApiResult.GenericError -> {

            }
            is ApiResult.Success -> {

            }
        }
        return result
    }
}
