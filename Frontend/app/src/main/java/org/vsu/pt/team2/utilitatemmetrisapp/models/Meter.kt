package org.vsu.pt.team2.utilitatemmetrisapp.models

import org.vsu.pt.team2.utilitatemmetrisapp.api.model.Metric

class Meter(
    var identifier: String,
    var type: MeterType,
    var tariff: Double,
    var prevMonthData: Double,
    var curMonthData: Double,
    var balance: Double,
) {
    constructor(
        metric: Metric
    ) : this(
        metric.identifier,
        metric.typeMetric,
        metric.tariff,
        metric.prevValue,
        metric.curValue,
        metric.balance
    )
}