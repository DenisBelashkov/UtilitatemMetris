package org.vsu.pt.team2.utilitatemmetrisapp.managers

import org.vsu.pt.team2.utilitatemmetrisapp.models.Meter
import org.vsu.pt.team2.utilitatemmetrisapp.network.BaseWorker
import org.vsu.pt.team2.utilitatemmetrisapp.repository.MeterRepo
import javax.inject.Inject

class MeterManager @Inject constructor(
    val baseWorker: BaseWorker,
    val meterRepo: MeterRepo
) {
    //хочется DI

    suspend fun updateMeters(identifier: String) {
        val res = baseWorker.metrics(identifier)
        if (res.isSuccess()) {
            val newData = res.data?.let {
                meterRepo.clear()
                meterRepo.addMeters(it.map { Meter(it) })
            }
        }
    }

    suspend fun getMeters(identifier: String? = null): List<Meter> {
        return meterRepo.meters().also {
            identifier?.let { ident ->
               it.filter { it.identifier == ident }
            }
        }
    }

    fun findMeters(identifier: String) {

    }
}