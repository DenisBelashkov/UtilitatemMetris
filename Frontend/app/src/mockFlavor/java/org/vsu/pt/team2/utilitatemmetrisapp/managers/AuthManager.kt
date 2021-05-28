package org.vsu.pt.team2.utilitatemmetrisapp.managers

import com.orhanobut.logger.Logger
import org.vsu.pt.team2.utilitatemmetrisapp.api.model.SuccessfulLoginUser
import org.vsu.pt.team2.utilitatemmetrisapp.models.User
import org.vsu.pt.team2.utilitatemmetrisapp.network.ApiResult
import javax.inject.Inject

class AuthManager @Inject constructor(
    val sessionManager: SessionManager
) {

    suspend fun authUser(email: String, pass: String): ApiResult<SuccessfulLoginUser> {
        Logger.d("Normal auth. Got email $email, pass $pass")
        val result : ApiResult<SuccessfulLoginUser> = ApiResult.Success<SuccessfulLoginUser>(
            SuccessfulLoginUser(
                "email_offline_demo",
                1,
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
                    sessionManager.setSession(User(this.id, this.email, this.token), false)
                }

            }
        }
        return result
    }

    suspend fun authUser(email: String): ApiResult<SuccessfulLoginUser> {
        Logger.d("Demo auth. Got email $email")
        val result : ApiResult<SuccessfulLoginUser> = ApiResult.Success<SuccessfulLoginUser>(
            SuccessfulLoginUser(
                "email_offline_demo",
                1,
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
                    sessionManager.setSession(User(this.id, this.email, this.token), true)
                }

            }
        }
        return result
    }
}
