package org.vsu.pt.team2.utilitatemmetrisapp.api.model

data class ItemPaymentHistory(
    val id: Int,
    val identifier: String,
    val date: String,
    val email: String,
    val cost: Number,
    val prevValue: Int,
    val curValue: Int
)