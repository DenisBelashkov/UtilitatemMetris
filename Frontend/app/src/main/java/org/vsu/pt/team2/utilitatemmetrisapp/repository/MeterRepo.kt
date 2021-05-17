package org.vsu.pt.team2.utilitatemmetrisapp.repository

import org.vsu.pt.team2.utilitatemmetrisapp.models.Meter


class MeterRepo {
    private val meters = mutableListOf<Meter>()

    fun clear() {
        meters.clear()
    }

    fun addMeter(meter: Meter) {
        meters.add(meter)
    }

    fun addMeter(meters: List<Meter>) {
        this.meters.addAll(meters)
    }

    fun deleteMeter(meter: Meter) {
        meters.remove(meter)
    }

    fun deleteMeter(identifier: String) {
        meters.find { it.identifier == identifier }?.let {
            deleteMeter(it)
        }
    }

}