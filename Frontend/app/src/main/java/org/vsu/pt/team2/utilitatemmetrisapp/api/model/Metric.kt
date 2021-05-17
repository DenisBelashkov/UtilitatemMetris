package org.vsu.pt.team2.utilitatemmetrisapp.api.model

data class Metric(
        val identifier: String,
        val balance: Int,
        val prevValue: Int,
        val curValue: Int,
        val tariff: Double,
        /**
         * One of [ electric, water, gas, heating ]
         */
        val typeMetric: String,
)