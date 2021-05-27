package org.vsu.pt.team2.utilitatemmetrisapp.api.model

data class CurrentMetric(
        val id: Int,
        val idUser: Int,
        val nameUser: String,
        val currentValue: Int
)