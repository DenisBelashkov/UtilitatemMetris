package org.vsu.pt.team2.utilitatemmetrisapp.models

import org.vsu.pt.team2.utilitatemmetrisapp.api.model.InformationAboutPayment
import org.vsu.pt.team2.utilitatemmetrisapp.dateutils.DateFormatter
import java.util.*

data class PaymentsFilter(
    var identifierMetric: String? = null,
    var dateFrom: Date? = null,
    var dateTo: Date? = null,
    var meterType: MeterType? = null,
) {
    fun toNetworkModel(): InformationAboutPayment {
        return InformationAboutPayment(
            identifierMetric,
            dateFrom?.let { DateFormatter.toNetworkString(it) },
            dateTo?.let { DateFormatter.toNetworkString(it) },
            meterType
        )
    }
}