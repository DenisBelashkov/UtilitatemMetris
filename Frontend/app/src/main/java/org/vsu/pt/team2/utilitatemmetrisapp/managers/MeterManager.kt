package org.vsu.pt.team2.utilitatemmetrisapp.managers

import org.vsu.pt.team2.utilitatemmetrisapp.models.Meter
import org.vsu.pt.team2.utilitatemmetrisapp.network.GeneralWorker
import org.vsu.pt.team2.utilitatemmetrisapp.repository.MeterRepo
import javax.inject.Inject

class MeterManager @Inject constructor(
    val generalWorker: GeneralWorker,
    val meterRepo: MeterRepo
) {
    //хочется DI

    suspend fun updateMeters(identifier: String) {
        val res = generalWorker.metrics(identifier)
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