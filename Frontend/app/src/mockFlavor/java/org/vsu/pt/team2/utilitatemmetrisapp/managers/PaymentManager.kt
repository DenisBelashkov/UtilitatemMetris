package org.vsu.pt.team2.utilitatemmetrisapp.managers

import org.vsu.pt.team2.utilitatemmetrisapp.models.PaymentData
import org.vsu.pt.team2.utilitatemmetrisapp.models.PaymentsFilter
import org.vsu.pt.team2.utilitatemmetrisapp.network.ApiResult
import org.vsu.pt.team2.utilitatemmetrisapp.offlineTools.PaymentManagerSupport
import org.vsu.pt.team2.utilitatemmetrisapp.repository.MeterRepo
import org.vsu.pt.team2.utilitatemmetrisapp.repository.PaymentRepo
import javax.inject.Inject

class PaymentManager @Inject constructor(
    val paymentRepo: PaymentRepo,
    val meterRepo: MeterRepo,
    val paymentManagerSupport: PaymentManagerSupport
) {
    suspend fun doPayment(
        meterIdentifiers: List<String>,
        cost: Double
    ): ApiResult<PaymentData> {
        val paymentData = paymentManagerSupport.doPayment(meterIdentifiers, cost)
        paymentRepo.addPayment(paymentData)
        return ApiResult.Success(paymentData)
//        val paymentInfo = Payment(meterIdentifiers, cost)
//        val res : ApiResult<List<ItemPaymentHistory>> = ApiResult.Success(listOf())
//
//        return when (res) {
//            is ApiResult.NetworkError -> {
//                res
//            }
//            is ApiResult.GenericError -> {
//                res
//            }
//            is ApiResult.Success -> {
//                val paymentDatas = res.value.map { PaymentData(it) }
//                paymentRepo.addPayments(paymentDatas)
//                return ApiResult.Success(paymentDatas)
//            }
//        }
    }

    suspend fun paymentHistory(
        filter: PaymentsFilter
    ): ApiResult<List<PaymentData>> {
        //todo сделать offline tools которые будут (изначально генерить и) выдавать платёжки
        // существующих счётчиков из репозитория
        return ApiResult.Success(paymentRepo.payments().toList())
//        val res =
//            generalWorker.paymentHistory(
//                filter.toNetworkModel()
//            )
//        return when (res) {
//            is ApiResult.NetworkError -> {
//                res
//            }
//            is ApiResult.GenericError -> {
//                res
//            }
//            is ApiResult.Success -> {
//                val paymentDatas = res.value.map { PaymentData(it) }
//                paymentRepo.addPayments(paymentDatas)
//                return ApiResult.Success(paymentDatas)
//            }
//        }
    }
}