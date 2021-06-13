package org.vsu.pt.team2.utilitatemmetrisapp.api.model

data class Payment(
    val metrics: List<String> = listOf(),
    val cost: Double = 0.0,
    val identifierForRemainCost: String = ""
)