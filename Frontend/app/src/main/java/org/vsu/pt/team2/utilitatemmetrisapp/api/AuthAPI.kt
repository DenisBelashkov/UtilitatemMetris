package org.vsu.pt.team2.utilitatemmetrisapp.api

import org.vsu.pt.team2.utilitatemmetrisapp.api.model.LoginUser
import org.vsu.pt.team2.utilitatemmetrisapp.api.model.QuickLoginUser
import org.vsu.pt.team2.utilitatemmetrisapp.api.model.SuccessfulLoginUser
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthAPI {
    @POST("/login/user")
    suspend fun login( @Body body: LoginUser) : Response<SuccessfulLoginUser>

    @POST("/login/quick")
    suspend fun login( @Body body: QuickLoginUser) : Response<SuccessfulLoginUser>
}