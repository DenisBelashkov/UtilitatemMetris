package org.vsu.pt.team2.utilitatemmetrisapp.managers

import com.orhanobut.logger.Logger
import com.yandex.metrica.YandexMetrica
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
        Logger.d("Загрузка счётчика, идентификатор : $identifier")
        YandexMetrica.reportEvent(
            "Загрузка счётчика, запрос",
            mapOf(
                "identifier" to identifier
            )
        )
        val res = generalWorker.metricByIdentifier(identifier)
        return when (res) {
            is ApiResult.NetworkError -> {
                Logger.d("Загрузка счётчика, ошибка")
                res
            }
            is ApiResult.GenericError -> {
                Logger.d("Загрузка счётчика, ошибка. ${res.code} ${res.error}")
                res
            }
            is ApiResult.Success -> {
                val meter = Meter(res.value.metric)
                Logger.d("Загрузка счётчика, успех. $meter")
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
        Logger.d("Загрузка сохранённых счётчиков юзера")
        YandexMetrica.reportEvent("Загрузка сохранённых счётчиков, запрос")
        val res = generalWorker.metricsSavedByUser()
        return when (res) {
            is ApiResult.NetworkError -> {
                Logger.d("Загрузка сохранённых счётчиков юзера, ошибка")
                res
            }
            is ApiResult.GenericError -> {
                Logger.d("Загрузка сохранённых счётчиков юзера, ошибка. ${res.error}")
                res
            }
            is ApiResult.Success -> {
                val meters = res.value.map { Meter(it) }
                Logger.d("Загрузка сохранённых счётчиков юзера, успех. $meters")
                meterRepo.addMeters(meters)
                return ApiResult.Success(meters)
            }
        }
    }

    suspend fun updateMeterData(identifier: String, currentValue: Double): ApiResult<*> {
        YandexMetrica.reportEvent(
            "Обновление данных счётчика, запрос",
            mapOf(
                "identifier" to identifier,
                "Current data" to currentValue,
            )
        )
        val res = generalWorker.updateMetric(CurrentMetric(identifier, currentValue))
        return res
    }

    suspend fun saveMeter(identifier: String): ApiResult<*> {
        Logger.d("Сохранение счётчика, $identifier")
        YandexMetrica.reportEvent(
            "Сохранение счётчика, запрос",
            mapOf(
                "identifier" to identifier
            )
        )
        val res = generalWorker.saveMetric(identifier)
        when (res){
            is ApiResult.NetworkError -> {
                Logger.d("Сохранение счётчика, ошибка")
                YandexMetrica.reportEvent(
                    "Сохранение счётчика, ошибка",
                    mapOf(
                        "identifier" to identifier,
                        "error" to "Network error"
                    )
                )
            }
            is ApiResult.GenericError -> {
                Logger.d("Сохранение счётчика, ошибка. ${res.error}")
                YandexMetrica.reportEvent(
                    "Сохранение счётчика, ошибка",
                    mapOf(
                        "identifier" to identifier,
                        "error" to "Generic error",
                        "code" to res.code,
                        "GenericError" to res.error,
                    )
                )
            }
            is ApiResult.Success-> {
                Logger.d("Сохранение счётчика, успех. $identifier")
                YandexMetrica.reportEvent(
                    "Сохранение счётчика, успех",
                    mapOf(
                        "identifier" to identifier
                    )
                )
            }
        }
        return res
    }

    suspend fun deleteMeter(identifier: String): ApiResult<*> {
        Logger.d("Удаление счётчика из сохранённых, $identifier")
        YandexMetrica.reportEvent(
            "Удаление счётчика из сохранённых, запрос",
            mapOf(
                "identifier" to identifier
            )
        )
        val res = generalWorker.deleteMetric(identifier)
        when (res){
            is ApiResult.NetworkError -> {
                Logger.d("Удаление счётчика из сохранённых, ошибка.")
                YandexMetrica.reportEvent(
                    "Удаление счётчика из сохранённых, ошибка",
                    mapOf(
                        "identifier" to identifier,
                        "error" to "Network error"
                    )
                )
            }
            is ApiResult.GenericError -> {
                Logger.d("Удаление счётчика из сохранённых, ошибка. ${res.error}")
                YandexMetrica.reportEvent(
                    "Удаление счётчика из сохранённых, ошибка",
                    mapOf(
                        "identifier" to identifier,
                        "error" to "Generic error",
                        "code" to res.code,
                        "GenericError" to res.error,
                    )
                )
            }
            is ApiResult.Success-> {
                Logger.d("Удаление счётчика из сохранённых, успех. $identifier")
                YandexMetrica.reportEvent(
                    "Удаление счётчика из сохранённых, успех",
                    mapOf(
                        "identifier" to identifier
                    )
                )
            }
        }
        return res
    }
}