package org.vsu.pt.team2.utilitatemmetrisapp.managers

import org.vsu.pt.team2.utilitatemmetrisapp.models.Meter
import org.vsu.pt.team2.utilitatemmetrisapp.network.ApiResult
import org.vsu.pt.team2.utilitatemmetrisapp.network.GeneralWorker
import org.vsu.pt.team2.utilitatemmetrisapp.repository.MeterRepo
import javax.inject.Inject

class MeterManager @Inject constructor(
    val generalWorker: GeneralWorker,
    val meterRepo: MeterRepo
) {

    suspend fun updateMeters(identifier: String) {
        val res = generalWorker.metrics(identifier)
        when (res) {
            is ApiResult.NetworkError -> {
                /*showtoast internet lost*/
            }
            is ApiResult.GenericError -> {

            }
            is ApiResult.Success -> {
                meterRepo.clear()
                meterRepo.addMeters(res.value.map { Meter(it) })
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