package org.vsu.pt.team2.utilitatemmetrisapp.network

import org.vsu.pt.team2.utilitatemmetrisapp.api.CommonAPI
import org.vsu.pt.team2.utilitatemmetrisapp.api.model.*
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseWorker @Inject constructor(
    val retrofit: Retrofit
) : ApiWorker() {
    private val commonAPI = retrofit.create(CommonAPI::class.java)

    suspend fun accounts(userId: Int): ApiResult<List<Flat>> {
        return safeApiCall { commonAPI.getAccounts(userId) }
    }

    suspend fun metrics(flatIdentifier: String): ApiResult<List<Metric>> {
        return safeApiCall { commonAPI.getMetrics(flatIdentifier) }
    }

    suspend fun updateMetric(currentMetric: CurrentMetric): ApiResult<*> {
        return safeApiCall { commonAPI.updateMetric(currentMetric) }
    }

    suspend fun paymentHistory(informationAboutPayment: InformationAboutPayment): ApiResult<List<ItemPaymentHistory>> {
        return safeApiCall { commonAPI.paymentHistory(informationAboutPayment) }
    }

    suspend fun toPay(payment: Payment): ApiResult<List<ItemPaymentHistory>> {
        return safeApiCall { commonAPI.toPay(payment) }
    }
}