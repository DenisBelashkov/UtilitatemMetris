package org.vsu.pt.team2.utilitatemmetrisapp.api.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.vsu.pt.team2.utilitatemmetrisapp.managers.SessionManager

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder: Request.Builder = chain.request().newBuilder()
        if (!SessionManager.isDemo) {
            requestBuilder.addHeader(HEADER_AUTH_TOKEN, SessionManager.jwt)
        }
        return chain.proceed(requestBuilder.build())
    }

    companion object {
        const val HEADER_AUTH_TOKEN = "X-Auth-Token"
    }
}