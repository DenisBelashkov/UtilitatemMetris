package org.vsu.pt.team2.utilitatemmetrisapp.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object ApiRetrofitBuilder {

    val objectMapper: ObjectMapper = ObjectMapper()
//            .setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE)

    fun build(): Retrofit {
        val client = OkHttpClientBuilder.build()

        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build()
    }
}