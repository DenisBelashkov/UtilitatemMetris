package org.vsu.pt.team2.utilitatemmetrisapp.managers

import org.vsu.pt.team2.utilitatemmetrisapp.api.model.LoginUser
import org.vsu.pt.team2.utilitatemmetrisapp.api.model.QuickLoginUser
import org.vsu.pt.team2.utilitatemmetrisapp.api.model.SuccessfulLoginUser
import org.vsu.pt.team2.utilitatemmetrisapp.models.User
import org.vsu.pt.team2.utilitatemmetrisapp.network.ApiResult
import org.vsu.pt.team2.utilitatemmetrisapp.network.AuthWorker
import javax.inject.Inject

class AuthManager @Inject constructor(
    val authWorker: AuthWorker,
    val sessionManager: SessionManager
) {

    suspend fun authUser(email: String, pass: String): ApiResult<SuccessfulLoginUser> {
        val result = authWorker.login(LoginUser(email, pass))
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
        val result = authWorker.login(QuickLoginUser(email))
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
