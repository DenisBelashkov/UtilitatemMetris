package org.vsu.pt.team2.utilitatemmetrisapp.managers

import org.vsu.pt.team2.utilitatemmetrisapp.api.model.Payment
import org.vsu.pt.team2.utilitatemmetrisapp.models.PaymentData
import org.vsu.pt.team2.utilitatemmetrisapp.models.PaymentsFilter
import org.vsu.pt.team2.utilitatemmetrisapp.network.ApiResult
import org.vsu.pt.team2.utilitatemmetrisapp.network.GeneralWorker
import org.vsu.pt.team2.utilitatemmetrisapp.repository.PaymentRepo
import javax.inject.Inject

class PaymentManager @Inject constructor(
    val generalWorker: GeneralWorker,
    val paymentRepo: PaymentRepo
) {
    suspend fun doPayment(
        meterIdentifiers: List<String>,
        cost: Double
    ): ApiResult<PaymentData> {
        val res =
            generalWorker.doPayment(Payment(cost, meterIdentifiers))
        return when (res) {
            is ApiResult.NetworkError -> {
                res
            }
            is ApiResult.GenericError -> {
                res
            }
            is ApiResult.Success -> {
                val paymentData = PaymentData(res.value)
                paymentRepo.addPayment(paymentData)
                return ApiResult.Success(paymentData)
            }
        }
    }

    suspend fun paymentHistory(
        filter: PaymentsFilter
    ): ApiResult<List<PaymentData>> {
        val res =
            generalWorker.paymentHistory(
                filter.toNetworkModel()
            )
        return when (res) {
            is ApiResult.NetworkError -> {
                res
            }
            is ApiResult.GenericError -> {
                res
            }
            is ApiResult.Success -> {
                val paymentDatas = res.value.map { PaymentData(it) }
                paymentRepo.addPayments(paymentDatas)
                return ApiResult.Success(paymentDatas)
            }
        }
    }
}