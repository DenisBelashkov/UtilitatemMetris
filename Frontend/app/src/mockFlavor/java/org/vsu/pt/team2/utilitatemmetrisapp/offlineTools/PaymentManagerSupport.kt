package org.vsu.pt.team2.utilitatemmetrisapp.offlineTools

import org.vsu.pt.team2.utilitatemmetrisapp.dateutils.DateFormatter
import org.vsu.pt.team2.utilitatemmetrisapp.managers.SessionManager
import org.vsu.pt.team2.utilitatemmetrisapp.models.Meter
import org.vsu.pt.team2.utilitatemmetrisapp.models.PaymentData
import org.vsu.pt.team2.utilitatemmetrisapp.models.PaymentMetricData
import org.vsu.pt.team2.utilitatemmetrisapp.repository.MeterRepo
import org.vsu.pt.team2.utilitatemmetrisapp.repository.PaymentRepo
import java.util.*
import javax.inject.Inject

class PaymentManagerSupport @Inject constructor(
    private val random: RandomTools,
    private val paymentRepo: PaymentRepo,
    private val meterRepo: MeterRepo,
    private val meterManagerSupport: MeterManagerSupport,
    private val sessionManager: SessionManager
) {
    suspend fun doPayment(
        meterIdentifiers: List<String>,
        cost: Double
    ): PaymentData {
        val paymentList = mutableListOf<PaymentMetricData>().also { list ->
            meterIdentifiers.forEach {
                val meter = meterRepo.findMeter(it) ?: Meter(meterManagerSupport.randomMetric(it))
                list.add(
                    PaymentMetricData(
                        meter,
                        meter.prevMonthData,
                        meter.curMonthData,
                        -meter.balance
                    )
                )
            }
        }
        return PaymentData(
            random.nextInt(),
            paymentList,
            DateFormatter.networkDateFormat.format(Date()),
            sessionManager.user.email
        )
    }
}