package org.vsu.pt.team2.utilitatemmetrisapp.api

import org.vsu.pt.team2.utilitatemmetrisapp.api.model.*
import retrofit2.Response
import retrofit2.http.*

interface CommonAPI {


    /**
     * Continue register
     */
    @POST("/register/continue")
    suspend fun continueRegister(@Body continueRegisterUser: ContinueRegisterUser)

    
    /**
     * Account
     */

    @GET("/flat")
    suspend fun getFlats(): List<Flat>


    /**
     * Metrics
     */

    @GET("/metrics/byFlatId/{identifier}")
    suspend fun getMetricsByFlatIdentifier(
        @Path("identifier") flatIdentifier: String
    ): List<Metric>

    @GET("/metrics/byId/{identifier}")
    suspend fun getMetricsByIdentifier(
        @Path("identifier") metricIdentifier: String
    ): Metric

    @GET("/metrics/byUser")
    suspend fun getMetricsSavedByUser(): List<Metric>

    @PUT("/metrics/update")
    suspend fun updateMetric(
        @Body currentMetric: CurrentMetric
    )

    @POST("/metric/{identifier}")
    suspend fun saveMetric(
        @Path("identifier") identifier: String
    )

    @DELETE("/metric/{identifier}")
    suspend fun deleteMetric(
        @Path("identifier") identifier: String
    )

    /**
     * Payment
     */
    @POST("/payment/metrics")
    suspend fun payment(
        @Body payment: Payment
    ): List<ItemPaymentHistory>


    @GET("/payment/history")
    suspend fun paymentHistory(
        @Body informationAboutPayment: InformationAboutPayment
    ): List<ItemPaymentHistory>
}