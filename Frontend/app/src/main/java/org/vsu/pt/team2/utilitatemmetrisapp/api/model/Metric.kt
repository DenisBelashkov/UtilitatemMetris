package org.vsu.pt.team2.utilitatemmetrisapp.api.model

import org.vsu.pt.team2.utilitatemmetrisapp.models.MeterType

data class Metric(
        val id: String = "",
        val balance: Double = 0.0,
        val prevValue: Double = 0.0,
        val currValue: Double = 0.0,
        val tariff: Double = 0.0,
        /**
         * One of [ electric, water, gas, heating ]
         */
        val typeMetric: MeterType = MeterType.ColdWater,
        val address: String = ""
)