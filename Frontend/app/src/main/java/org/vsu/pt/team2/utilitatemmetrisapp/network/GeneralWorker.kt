package org.vsu.pt.team2.utilitatemmetrisapp.network

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import org.vsu.pt.team2.utilitatemmetrisapp.api.CommonAPI
import org.vsu.pt.team2.utilitatemmetrisapp.api.model.*
import retrofit2.Retrofit
import javax.inject.Inject

class GeneralWorker @Inject constructor(
    val retrofit: Retrofit,
) : ApiWorker() {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
    private val commonAPI = retrofit.create(CommonAPI::class.java)

    suspend fun accounts(userId: Int): ApiResult<List<Flat>> {
        return safeApiCall(dispatcher) { commonAPI.getAccounts(userId) }
    }

    suspend fun metricsByAccountIdentifier(flatIdentifier: String): ApiResult<List<Metric>> {
        return safeApiCall(dispatcher) { commonAPI.getMetrics(flatIdentifier) }
    }

    //todo
    suspend fun meterByIdentifier(metricIdentifier: String) : ApiResult<Metric> {
        delay(1000L)
        return ApiResult.NetworkError
    }

    suspend fun updateMetric(currentMetric: CurrentMetric): ApiResult<*> {
        return safeApiCall(dispatcher) { commonAPI.updateMetric(currentMetric) }
    }

    suspend fun paymentHistory(informationAboutPayment: InformationAboutPayment): ApiResult<List<ItemPaymentHistory>> {
        return safeApiCall(dispatcher) { commonAPI.paymentHistory(informationAboutPayment) }
    }

    suspend fun toPay(payment: Payment): ApiResult<List<ItemPaymentHistory>> {
        return safeApiCall(dispatcher) { commonAPI.toPay(payment) }
    }

    //todo
    suspend fun favoriteMeter(identifier: String): ApiResult<*> {
        delay(1000L)
        return ApiResult.Success<Any>(Any())
    }

    //todo
    suspend fun unfavoriteMeter(identifier: String): ApiResult<*> {
        delay(1000L)
        return ApiResult.Success<Any>(Any())
    }

    //todo
    suspend fun metricsByUserId(userId: Int): ApiResult<List<Metric>> {
        delay(1000L)
        return ApiResult.NetworkError
    }
}