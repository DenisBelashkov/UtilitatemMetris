package org.vsu.pt.team2.utilitatemmetrisapp.api.model

import org.vsu.pt.team2.utilitatemmetrisapp.models.MeterType

data class Metric(
        val identifier: String,
        val balance: Double,
        val prevValue: Double,
        val curValue: Double,
        val tariff: Double,
        /**
         * One of [ electric, water, gas, heating ]
         */
        val typeMetric: MeterType,
)