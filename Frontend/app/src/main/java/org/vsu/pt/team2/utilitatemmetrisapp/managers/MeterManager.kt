package org.vsu.pt.team2.utilitatemmetrisapp.managers

import org.vsu.pt.team2.utilitatemmetrisapp.models.Meter
import org.vsu.pt.team2.utilitatemmetrisapp.network.ApiResult
import org.vsu.pt.team2.utilitatemmetrisapp.network.GeneralWorker
import org.vsu.pt.team2.utilitatemmetrisapp.repository.SavedMeterRepo
import java.lang.NullPointerException
import javax.inject.Inject

class MeterManager @Inject constructor(
    val generalWorker: GeneralWorker,
    val savedMeterRepo: SavedMeterRepo,
    val sessionManager: SessionManager,
) {

    suspend fun loadUserMeters(): ApiResult<List<Meter>> {
        val res = generalWorker.metricsByUserId(sessionManager.user.id)
        return when (res) {
            is ApiResult.Success -> {
                val meters = res.value.map { Meter(it, false) }
                savedMeterRepo.clear()
                savedMeterRepo.addMeters(meters)
                ApiResult.Success<List<Meter>>(meters)
            }
            is ApiResult.NetworkError -> res
            is ApiResult.GenericError -> res
        }
    }

    suspend fun requiredMeter(identifier: String): Meter {
        val meter: Meter?
        val res = loadMeter(identifier)
        when (res) {
            is ApiResult.Success ->
                meter = res.value
            is ApiResult.GenericError, ApiResult.NetworkError ->
                meter = savedMeterRepo.meterOrNull(identifier)
        }
        return meter ?: throw NullPointerException("Meter not found by identifier ${identifier}")
    }

    suspend fun loadMeter(identifier: String): ApiResult<Meter> {
        //todo change to load 1 meter endpoint, not many
        val res = generalWorker.meterByIdentifier(identifier)
        return when (res) {
            is ApiResult.Success ->
                ApiResult.Success<Meter>(Meter(res.value, false))
            is ApiResult.NetworkError -> res
            is ApiResult.GenericError -> res
        }
    }

    suspend fun changeMeterFavorite(meter: Meter, isFav: Boolean): ApiResult<*> {
        val res = if (isFav)
            generalWorker.favoriteMeter(meter.identifier)
        else
            generalWorker.unfavoriteMeter(meter.identifier)

        when (res) {
            is ApiResult.Success -> {
                if (isFav) {
                    savedMeterRepo.addMeter(meter)
                } else {
                    savedMeterRepo.deleteMeter(meter)
                }
            }
        }
        return res
    }
}