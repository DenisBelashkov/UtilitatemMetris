package org.vsu.pt.team2.utilitatemmetrisapp.viewmodels

import org.vsu.pt.team2.utilitatemmetrisapp.models.Meter
import org.vsu.pt.team2.utilitatemmetrisapp.models.MeterType

class MeterViewModel(
    var identifier: String,
    var type: MeterType,
    var tariff: Double,
    var prevMonthData: Double,
    var curMonthData: Double,
    var backlog: Double,
) {

    fun toMeterItemVM(): MeterItemViewModel {
        return MeterItemViewModel(
            identifier,
            type,
            backlog
        )
    }

    companion object {
        fun fromMeterItemVM(
            mivm: MeterItemViewModel,
            tariff: Double,
            prevMonthData: Double,
            curMonthData: Double
        ): MeterViewModel {
            return MeterViewModel(
                mivm.identifier,
                mivm.type,
                tariff,
                prevMonthData,
                curMonthData,
                mivm.backlog
            )
        }

        fun fromMeter(
            meter: Meter
        ): MeterViewModel {
            return MeterViewModel(
                meter.identifier,
                meter.type,
                meter.tariff,
                meter.prevMonthData,
                meter.curMonthData,
                -meter.balance
            )
        }
    }
}