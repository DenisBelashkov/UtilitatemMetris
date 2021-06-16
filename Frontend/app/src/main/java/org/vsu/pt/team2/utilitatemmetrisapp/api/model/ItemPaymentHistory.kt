package org.vsu.pt.team2.utilitatemmetrisapp.api.model

data class ItemPaymentHistory(
    val id: Int = 0,
    val metrics: List<ItemMeterPaymentHistory> = listOf(),
    val date: String = "",
    val email: String = "",
)

data class ItemMeterPaymentHistory(
    val metric: Metric = Metric(),
    val prevValue: Double = 0.0,
    val curValue: Double = 0.0,
    val cost: Double = 0.0
)