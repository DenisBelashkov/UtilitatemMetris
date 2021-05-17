package org.vsu.pt.team2.utilitatemmetrisapp.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.vsu.pt.team2.utilitatemmetrisapp.BuildConfig
import org.vsu.pt.team2.utilitatemmetrisapp.api.interceptor.AuthInterceptor
import org.vsu.pt.team2.utilitatemmetrisapp.api.interceptor.CommonDataInterceptor
import java.util.concurrent.TimeUnit

object OkHttpClientBuilder {
    /**
     * Таймаут на подключение/чтение/запись (в секундах)
     */
    private const val TIMEOUT: Long = 30


    fun build(): OkHttpClient {

        return OkHttpClient().newBuilder()
                .addInterceptor(AuthInterceptor())
                .addInterceptor(CommonDataInterceptor())
                .addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = if (BuildConfig.DEBUG)
                                HttpLoggingInterceptor.Level.BODY
                            else
                                HttpLoggingInterceptor.Level.NONE
                        }
                )
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build()
    }

}