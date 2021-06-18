package org.vsu.pt.team2.utilitatemmetrisapp.network

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.vsu.pt.team2.utilitatemmetrisapp.api.CommonAPI
import org.vsu.pt.team2.utilitatemmetrisapp.api.model.*
import retrofit2.Retrofit
import javax.inject.Inject

class GeneralWorker @Inject constructor(
    val retrofit: Retrofit,
) : ApiWorker() {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
    private val commonAPI = retrofit.create(CommonAPI::class.java)

    suspend fun flats(): ApiResult<List<Flat>> {
        return safeApiCall(dispatcher) { commonAPI.getFlats() }
    }

    suspend fun metricsByFlat(flatIdentifier: String): ApiResult<List<Metric>> {
        return safeApiCall(dispatcher) { commonAPI.getMetricsByFlatIdentifier(flatIdentifier) }
    }

    suspend fun metricByIdentifier(metricIdentifier: String): ApiResult<MetricWithSavedField> {
        return safeApiCall(dispatcher) { commonAPI.getMetricsByIdentifier(metricIdentifier) }
    }

    suspend fun metricsSavedByUser(): ApiResult<List<Metric>> {
        return safeApiCall(dispatcher) { commonAPI.getMetricsSavedByUser() }
    }

    suspend fun updateMetric(currentMetric: CurrentMetric): ApiResult<*> {
        return safeApiCall(dispatcher) { commonAPI.updateMetric(currentMetric) }
    }

    suspend fun saveMetric(identifier: String): ApiResult<*> {
        return safeApiCall(dispatcher) { commonAPI.saveMetric(identifier) }
    }

    suspend fun deleteMetric(identifier: String): ApiResult<*> {
        return safeApiCall(dispatcher) { commonAPI.deleteMetric(identifier) }
    }

    suspend fun doPayment(payment: Payment): ApiResult<ItemPaymentHistory> {
        return safeApiCall(dispatcher) { commonAPI.payment(payment) }
    }

    suspend fun paymentHistory(informationAboutPayment: InformationAboutPayment): ApiResult<List<ItemPaymentHistory>> {
        return safeApiCall(dispatcher) { commonAPI.paymentHistory(informationAboutPayment) }
    }
}