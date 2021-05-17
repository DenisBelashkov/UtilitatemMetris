package org.vsu.pt.team2.utilitatemmetrisapp.models

import android.content.Context
import org.vsu.pt.team2.utilitatemmetrisapp.R

enum class MeterType {
    ColdWater,
    HotWater,
    Gas,
    Heating,
    Elect;

    fun toLanguagedString(context: Context): String {
        return when (this) {
            Elect -> context.getString(R.string.meter_type_electricity)
            ColdWater -> context.getString(R.string.meter_type_cold_water)
            HotWater -> context.getString(R.string.meter_type_hot_water)
            Gas -> context.getString(R.string.meter_type_gas)
            Heating -> context.getString(R.string.heating)
        }
    }

    companion object {
        fun random(): MeterType = MeterType.values().random()
    }
}