package org.vsu.pt.team2.utilitatemmetrisapp.models

import org.vsu.pt.team2.utilitatemmetrisapp.api.model.Metric

data class Meter(
    var identifier: String,
    var type: MeterType,
    var tariff: Double,
    var prevMonthData: Double,
    var curMonthData: Double,
    var balance: Double,
    var address: String
) {
    constructor(metric: Metric) : this(
        metric.identifier,
        metric.typeMetric,
        metric.tariff,
        metric.prevValue,
        metric.currValue,
        metric.balance,
        metric.address
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Meter

        if (identifier != other.identifier) return false

        return true
    }

    override fun hashCode(): Int {
        return identifier.hashCode()
    }


}