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
    ): List<Flat>


    /**
     * Metrics
     */

    @GET("/metrics/{flatId}")
    fun getMetrics(
            @Path("flatId") flatIdentifier: String
    ): List<Metric>

    @PUT("/metrics/update")
    fun updateMetric(
            @Body currentMetric: CurrentMetric
    ): Nothing


    /**
     * Payment
     */

    @POST("/payment/metrics")
    fun toPay(@Body body: Payment): List<ItemPaymentHistory>

    @GET("/payment/history")
    fun paymentHistory(
            @Body informationAboutPayment: InformationAboutPayment
    ): List<ItemPaymentHistory>
}