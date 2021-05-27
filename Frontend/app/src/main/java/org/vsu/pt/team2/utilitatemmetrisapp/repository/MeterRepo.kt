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

    private val meters: HashSet<Meter> = hashSetOf<Meter>()

    private val savedMeters: HashSet<Meter> = hashSetOf<Meter>()

    suspend fun meters(): Set<Meter> = meters.toSet()

    suspend fun savedMeters(): Set<Meter> = savedMeters.toSet()

    suspend fun meters(identifier: String): List<Meter> = meters.filter { it.identifier == identifier }

    suspend fun meterOrNull(identifier: String): Meter? = meters.firstOrNull { it.identifier == identifier }


    suspend fun clear() {
        meters.clear()
    }

    suspend fun addMeter(meter: Meter, isSaved: Boolean = false) {
        if (isSaved)
            savedMeters.add(meter)
        else
            meters.add(meter)
    }

    suspend fun addMeters(meters: List<Meter>, isSaved: Boolean = false) {
        if (isSaved)
            savedMeters.addAll(meters)
        else
            this.meters.addAll(meters)
    }

    suspend fun deleteMeter(meter: Meter) {
        meters.remove(meter)
        savedMeters.remove(meter)
    }

    suspend fun deleteMeterFromSaved(meter: Meter) {
        savedMeters.remove(meter)
    }

    suspend fun deleteMeter(identifier: String) {
        meters.find { it.identifier == identifier }?.let {
            deleteMeter(it)
        }
        savedMeters.find { it.identifier == identifier }?.let {
            deleteMeter(it)
        }
    }

    suspend fun deleteMeterFromSaved(identifier: String) {
        savedMeters.find { it.identifier == identifier }?.let {
            deleteMeter(it)
        }
    }

    suspend fun findMeter(identifier: String) =
        meters.find { it.identifier == identifier }
            ?: savedMeters.find { it.identifier == identifier }

}