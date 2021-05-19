package org.vsu.pt.team2.utilitatemmetrisapp.repository

import org.vsu.pt.team2.utilitatemmetrisapp.models.Meter


class MeterRepo {
    //todo если будет реальный репозиторий с бд, то сделать :
    /*
        private val dispatcher: CoroutineDispatcher = Dispatchers.IO
         withContext(dispatcher) {
            //code
         }
     */

    private val meters = mutableListOf<Meter>()

    suspend fun meters(): List<Meter> = meters

    suspend fun clear() {
        meters.clear()
    }

    suspend fun addMeter(meter: Meter) {
        meters.add(meter)
    }

    suspend fun addMeters(meters: List<Meter>) {
        this.meters.addAll(meters)
    }

    suspend fun deleteMeter(meter: Meter) {
        meters.remove(meter)
    }

    suspend fun deleteMeter(identifier: String) {
        meters.find { it.identifier == identifier }?.let {
            deleteMeter(it)
        }
    }

}