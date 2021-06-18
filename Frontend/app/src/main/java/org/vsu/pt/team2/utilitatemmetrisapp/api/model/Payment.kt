package org.vsu.pt.team2.utilitatemmetrisapp.api.model

data class Payment(
    val metrics: List<PaymentMetricIdentifier> = listOf<PaymentMetricIdentifier>(),
    val cost: Double = 0.0
) {
    constructor(cost: Double, metricIdentifiers: List<String>) : this(
        metricIdentifiers.map { PaymentMetricIdentifier(it) }, cost
    )
}

data class PaymentMetricIdentifier(
    val identifier: String
)