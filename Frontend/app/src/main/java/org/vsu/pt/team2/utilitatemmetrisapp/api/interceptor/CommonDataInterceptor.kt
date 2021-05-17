package org.vsu.pt.team2.utilitatemmetrisapp.api.interceptor

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

class CommonDataInterceptor : Interceptor {

    private val cacheControl = CacheControl.Builder().maxAge(1, TimeUnit.DAYS).build()

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
                .addHeader("Accept", "application/json")
//                .addHeader("X-API-Key", "6ae3777cc40e60f4b0426b8f3bf52c4b")
//                .addHeader("X-Device-ID", Util.getDeviceID())
//                .addHeader("User-Agent", Util.getUserAgent())
                .cacheControl(cacheControl)

        return chain.proceed(builder.build())
    }
}