package org.vsu.pt.team2.utilitatemmetrisapp.models

import android.content.Context
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import org.vsu.pt.team2.utilitatemmetrisapp.R
import java.lang.RuntimeException

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

    @JsonValue
    fun toValue(): String {
        return when (this) {
            Elect -> "electric"
            ColdWater -> "coldwater"
            HotWater -> "hotwater"
            Gas -> "gas"
            Heating -> "heating"
            else -> throw RuntimeException("Cant serialize Meter Type $this")
        }
    }

    companion object {
        fun random(): MeterType = MeterType.values().random()

        @JsonCreator
        fun forValue(value: String): MeterType {
            return when (value) {
                "electric" -> Elect
                "coldwater" -> ColdWater
                "hotwater" -> HotWater
                "gas" -> Gas
                "heating" -> Heating
                else -> throw RuntimeException("Cant deserialize Meter Type from string $value")
            }
        }
    }
}