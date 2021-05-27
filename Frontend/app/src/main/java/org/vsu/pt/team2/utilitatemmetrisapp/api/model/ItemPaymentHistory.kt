package org.vsu.pt.team2.utilitatemmetrisapp.api.model

data class ItemPaymentHistory(
        val id: Int,
        val date: String,
        val nameUser: String,
        val cost: Number,
        val prevValue: Int,
        val curValue: Int
)