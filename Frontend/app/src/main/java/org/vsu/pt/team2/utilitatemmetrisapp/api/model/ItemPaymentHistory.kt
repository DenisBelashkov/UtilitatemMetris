package org.vsu.pt.team2.utilitatemmetrisapp.api.model

data class ItemPaymentHistory(
    val id: Int = 0,
    val identifier: String = "",
    val date: String = "",
    val email: String = "",
    val cost: Double = 0.0,
    val prevValue: Int = 0,
    val curValue: Int = 0
)