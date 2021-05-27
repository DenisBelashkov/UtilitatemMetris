package org.vsu.pt.team2.utilitatemmetrisapp.managers

import org.vsu.pt.team2.utilitatemmetrisapp.api.model.InformationAboutPayment
import org.vsu.pt.team2.utilitatemmetrisapp.api.model.Payment
import org.vsu.pt.team2.utilitatemmetrisapp.models.MeterType
import org.vsu.pt.team2.utilitatemmetrisapp.models.PaymentData
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
        cost: Double,
        meterIdentifierForRemainCost: String
    ): ApiResult<List<PaymentData>> {
        val res =
            generalWorker.doPayment(Payment(meterIdentifiers, cost, meterIdentifierForRemainCost))
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

    suspend fun paymentHistory(
        //todo date
        dateWith: String,
        dateTo: String,
        type: MeterType
    ): ApiResult<List<PaymentData>> {
        val res =
            generalWorker.paymentHistory(
                InformationAboutPayment(
                    dateWith,
                    dateTo,
                    type.toValue()
                )
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