package org.vsu.pt.team2.utilitatemmetrisapp.viewmodels

import org.vsu.pt.team2.utilitatemmetrisapp.models.MeterType

class MeterViewModel(
    var identifier: String,
    var type: MeterType,
    var tariff: Double,
    var prevMonthData: Double,
    var curMonthData: Double,
    var backlog: Double,
    var isSaved: Boolean,
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
            curMonthData: Double,
            isSaved: Boolean
        ): MeterViewModel {
            return MeterViewModel(
                mivm.identifier,
                mivm.type,
                tariff,
                prevMonthData,
                curMonthData,
                mivm.backlog,
                isSaved
            )
        }
    }
}