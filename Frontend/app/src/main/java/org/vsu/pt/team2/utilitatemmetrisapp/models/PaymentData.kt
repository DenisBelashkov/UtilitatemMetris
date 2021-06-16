package org.vsu.pt.team2.utilitatemmetrisapp.models

import org.vsu.pt.team2.utilitatemmetrisapp.api.model.ItemMeterPaymentHistory
import org.vsu.pt.team2.utilitatemmetrisapp.api.model.ItemPaymentHistory
import org.vsu.pt.team2.utilitatemmetrisapp.dateutils.DateFormatter

data class PaymentData(
    val id: Int,
    val metricDatas: List<PaymentMetricData>,
    val date: String,
    val email: String
) {
    constructor(netModel: ItemPaymentHistory) : this(
        netModel.id,
        netModel.metrics.map { PaymentMetricData(it) },
        DateFormatter.fromNetworkStringToString(netModel.date),
        netModel.email
    )
}

data class PaymentMetricData(
    var meter: Meter,
    var prevValue: Double,
    var curValue: Double,
    val cost: Double
) {
    constructor(itemMeterPaymentHistory: ItemMeterPaymentHistory) :
            this(
                Meter(itemMeterPaymentHistory.metric),
                itemMeterPaymentHistory.prevValue,
                itemMeterPaymentHistory.curValue,
                itemMeterPaymentHistory.cost
            )
}
