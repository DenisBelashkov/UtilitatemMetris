package org.vsu.pt.team2.utilitatemmetrisapp.models

class Meter(
        var identifier: String,
        var type: MeterType,
        var tariff: Double,
        var prevMonthData: Double,
        var curMonthData: Double,
        var backlog: Double,
)