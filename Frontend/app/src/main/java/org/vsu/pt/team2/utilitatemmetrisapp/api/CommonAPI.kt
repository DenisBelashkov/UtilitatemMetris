package org.vsu.pt.team2.utilitatemmetrisapp.api

import org.vsu.pt.team2.utilitatemmetrisapp.api.model.*
import retrofit2.Response
import retrofit2.http.*

interface CommonAPI {


    /**
     * Continue register
     */
    @POST("/register/continue")
    fun continueRegister(@Body continueRegisterUser: ContinueRegisterUser)

    
    /**
     * Account
     */

    @GET("/flat")
    fun getFlats(): List<Flat>


    /**
     * Metrics
     */

    @GET("/metrics/byFlatId/{identifier}")
    fun getMetricsByFlatIdentifier(
        @Path("identifier") flatIdentifier: String
    ): List<Metric>

    @GET("/metrics/byId/{identifier}")
    fun getMetricsByIdentifier(
        @Path("identifier") metricIdentifier: String
    ): Metric

    @GET("/metrics/byUser")
    fun getMetricsSavedByUser(): List<Metric>

    @PUT("/metrics/update")
    fun updateMetric(
        @Body currentMetric: CurrentMetric
    )

    @POST("/metric/{identifier}")
    fun saveMetric(
        @Path("identifier") identifier: String
    )

    @DELETE("/metric/{identifier}")
    fun deleteMetric(
        @Path("identifier") identifier: String
    )

    /**
     * Payment
     */
    @POST("/payment/metrics")
    fun payment(
        @Body payment: Payment
    ): List<ItemPaymentHistory>


    @GET("/payment/history")
    fun paymentHistory(
        @Body informationAboutPayment: InformationAboutPayment
    ): List<ItemPaymentHistory>
}