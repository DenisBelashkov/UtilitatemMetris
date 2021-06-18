package org.vsu.pt.team2.utilitatemmetrisapp.api

import org.vsu.pt.team2.utilitatemmetrisapp.api.model.LoginUser
import org.vsu.pt.team2.utilitatemmetrisapp.api.model.QuickLoginUser
import org.vsu.pt.team2.utilitatemmetrisapp.api.model.RegisterUser
import org.vsu.pt.team2.utilitatemmetrisapp.api.model.SuccessfulLoginUser
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthAPI {
    @POST("/login/user")
    suspend fun login( @Body body: LoginUser) : SuccessfulLoginUser

    @POST("/login/quick")
    suspend fun login( @Body body: QuickLoginUser) : SuccessfulLoginUser

    @POST("/register/user")
    suspend fun register( @Body body: RegisterUser)
}