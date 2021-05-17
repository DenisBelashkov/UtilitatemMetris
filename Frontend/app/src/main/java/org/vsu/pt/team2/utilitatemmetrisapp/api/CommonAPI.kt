package org.vsu.pt.team2.utilitatemmetrisapp.api

import org.vsu.pt.team2.utilitatemmetrisapp.api.model.*
import retrofit2.Response
import retrofit2.http.*

interface CommonAPI {

    /**
     * Account
     */

    @GET("/flat/{userId}")
    fun getAccounts(
            @Path("userId") userId: Int
    ): Response<List<Flat>>


    /**
     * Metrics
     */

    @GET("/metrics/{flatId}")
    fun getMetrics(
            @Path("flatId") flatIdentifier: String
    ): Response<List<Metric>>

    @PUT("/metrics/update")
    fun updateMetric(
            @Body currentMetric: CurrentMetric
    ): Response<*>


    /**
     * Payment
     */

    @POST("/payment/metrics")
    fun toPay(@Body body: Payment): Response<List<ItemPaymentHistory>>

    @GET("/payment/history")
    fun paymentHistory(
            @Body informationAboutPayment: InformationAboutPayment
    ): Response<List<ItemPaymentHistory>>
}