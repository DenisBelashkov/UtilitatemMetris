package org.vsu.pt.team2.utilitatemmetrisapp.api.model

data class Payment(
    val metrics: List<String>,
    val cost: Number,
    val identifierForeRemainCost: String
)