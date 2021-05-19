package org.vsu.pt.team2.utilitatemmetrisapp.network

import kotlinx.coroutines.delay
import org.vsu.pt.team2.utilitatemmetrisapp.api.AuthAPI
import org.vsu.pt.team2.utilitatemmetrisapp.api.model.LoginUser
import org.vsu.pt.team2.utilitatemmetrisapp.api.model.QuickLoginUser
import org.vsu.pt.team2.utilitatemmetrisapp.api.model.SuccessfulLoginUser
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class AuthWorker @Inject constructor(
    //retrofit: Retrofit
) : ApiWorker() {

//    private val authApi = retrofit.create(AuthAPI::class.java)

    suspend fun login(loginUser: LoginUser) : ApiResult<SuccessfulLoginUser>{
        delay(2000L)
        return ApiResult<SuccessfulLoginUser>().apply {
            data = SuccessfulLoginUser(
                "emain",
                -1,
                "jwtjwtjwt"
            )
        }
    }

    suspend fun login(quickLoginUser: QuickLoginUser) : ApiResult<SuccessfulLoginUser>{
        delay(2000L)
        return ApiResult<SuccessfulLoginUser>().apply {
            data = SuccessfulLoginUser(
                "emain",
                -1,
                "jwtjwtjwt"
            )
        }
    }
}