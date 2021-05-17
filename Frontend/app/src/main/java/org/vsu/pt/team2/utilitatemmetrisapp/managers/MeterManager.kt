package org.vsu.pt.team2.utilitatemmetrisapp.managers

import org.vsu.pt.team2.utilitatemmetrisapp.network.BaseWorker
import org.vsu.pt.team2.utilitatemmetrisapp.repository.MeterRepo

object MeterManager {
    val meterRepo = MeterRepo()
    //хочется DI
//    var baseWorker: BaseWorker

    fun findMeters(identifier: String) {

    }
}