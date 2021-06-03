package org.vsu.pt.team2.utilitatemmetrisapp.managers

import org.vsu.pt.team2.utilitatemmetrisapp.api.model.CurrentMetric
import org.vsu.pt.team2.utilitatemmetrisapp.models.Meter
import org.vsu.pt.team2.utilitatemmetrisapp.network.ApiResult
import org.vsu.pt.team2.utilitatemmetrisapp.network.GeneralWorker
import org.vsu.pt.team2.utilitatemmetrisapp.repository.MeterRepo
import javax.inject.Inject

class MeterManager @Inject constructor(
    val generalWorker: GeneralWorker,
    val meterRepo: MeterRepo
) {

    suspend fun getMeterByIdentifier(identifier: String): ApiResult<Pair<Meter, Boolean>> {
        val res = generalWorker.metricByIdentifier(identifier)
        return when (res) {
            is ApiResult.NetworkError -> {
                res
            }
            is ApiResult.GenericError -> {
                res
            }
            is ApiResult.Success -> {
                val meter = Meter(res.value.metric)
                meterRepo.addMeter(meter, res.value.isSaved)
                return ApiResult.Success(Pair(meter, res.value.isSaved))
            }
        }
    }

    suspend fun getMeterByAccountIdentifier(accountIdentifier: String): ApiResult<List<Meter>> {
        val res = generalWorker.metricsByFlat(accountIdentifier)
        return when (res) {
            is ApiResult.NetworkError -> {
                res
            }
            is ApiResult.GenericError -> {
                res
            }
            is ApiResult.Success -> {
                val meters = res.value.map { Meter(it) }
                meterRepo.addMeters(meters)
                return ApiResult.Success(meters)
            }
        }
    }

    suspend fun getMetersSavedByUser(): ApiResult<List<Meter>> {
        val res = generalWorker.metricsSavedByUser()
        return when (res) {
            is ApiResult.NetworkError -> {
                res
            }
            is ApiResult.GenericError -> {
                res
            }
            is ApiResult.Success -> {
                val meters = res.value.map { Meter(it) }
                meterRepo.addMeters(meters)
                return ApiResult.Success(meters)
            }
        }
    }

    suspend fun updateMeterData(identifier: String, currentValue: Double): ApiResult<*> {
        val res = generalWorker.updateMetric(CurrentMetric(identifier, currentValue))
        return res
    }

    suspend fun saveMeter(identifier: String): ApiResult<*> {
        val res = generalWorker.saveMetric(identifier)
        return res
    }

    suspend fun deleteMeter(identifier: String): ApiResult<*> {
        val res = generalWorker.saveMetric(identifier)
        return res
    }
}