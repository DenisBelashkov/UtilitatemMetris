package org.vsu.pt.team2.utilitatemmetrisapp.managers

import org.vsu.pt.team2.utilitatemmetrisapp.api.model.CurrentMetric
import org.vsu.pt.team2.utilitatemmetrisapp.models.Meter
import org.vsu.pt.team2.utilitatemmetrisapp.network.ApiResult
import org.vsu.pt.team2.utilitatemmetrisapp.offlineTools.MeterManagerSupport
import org.vsu.pt.team2.utilitatemmetrisapp.repository.MeterRepo
import javax.inject.Inject

class MeterManager @Inject constructor(
    val meterManagerOfflineSupport: MeterManagerSupport,
    val meterRepo: MeterRepo
) {

    suspend fun getMeterByIdentifier(identifier: String): ApiResult<Pair<Meter, Boolean>> {

        meterRepo.findMeter(identifier)?.let {
            return ApiResult.Success<Pair<Meter, Boolean>>(
                Pair(
                    it,
                    meterRepo.savedMeters().contains(it)
                )
            )
        }
        val res = meterManagerOfflineSupport.meterByIdentifier(identifier)
        return when (res) {
            is ApiResult.NetworkError -> {
                res
            }
            is ApiResult.GenericError -> {
                res
            }
            is ApiResult.Success -> {
                val meter = Meter(res.value.first)
                meterRepo.addMeter(meter)
                return ApiResult.Success(Pair(meter, false))
            }
        }
    }

    suspend fun getMetersByAccountIdentifier(accountIdentifier: String): ApiResult<List<Meter>> {
        val res = meterManagerOfflineSupport.metersByAccountIdentifier(accountIdentifier)
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
        return ApiResult.Success(meterRepo.savedMeters().toList())
        /*val res = meterManagerOfflineSupport.metersSavedByUser();
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
        }*/
    }

    suspend fun updateMeterData(identifier: String, currentValue: Double): ApiResult<*> {
        val res =
            meterManagerOfflineSupport.updateMeterData(CurrentMetric(identifier, currentValue));
        meterRepo.findMeter(identifier)?.let {
            it.curMonthData = currentValue
        }
        return res
    }

    suspend fun saveMeter(identifier: String): ApiResult<*> {
        val res = meterManagerOfflineSupport.saveMeter(identifier)
        return when (res) {
            is ApiResult.NetworkError -> {
                res
            }
            is ApiResult.GenericError -> {
                res
            }
            is ApiResult.Success -> {
                meterRepo.findMeter(identifier)?.let {
                    meterRepo.addMeter(it, true)
                }
                return res
            }
        }
    }

    suspend fun deleteMeter(identifier: String): ApiResult<*> {
        val res = meterManagerOfflineSupport.deleteMeter(identifier)
        return when (res) {
            is ApiResult.NetworkError -> {
                res
            }
            is ApiResult.GenericError -> {
                res
            }
            is ApiResult.Success -> {
                meterRepo.findMeter(identifier)?.let {
                    meterRepo.deleteMeterFromSaved(it)
                }
                return res
            }
        }
    }
}