package org.vsu.pt.team2.utilitatemmetrisapp.offlineTools

import org.vsu.pt.team2.utilitatemmetrisapp.api.model.CurrentMetric
import org.vsu.pt.team2.utilitatemmetrisapp.api.model.Metric
import org.vsu.pt.team2.utilitatemmetrisapp.models.MeterType
import org.vsu.pt.team2.utilitatemmetrisapp.network.ApiResult
import javax.inject.Inject
import kotlin.random.Random

class MeterManagerSupport @Inject constructor(){
    private val random = Random.Default

    private fun randomIdentifier(): String = List(16) {
        if (it <= 5 || it > 10)
            (('a'..'z') + ('A'..'Z')).random()
        else
            ('0'..'9').random()
    }.joinToString("")

    private fun randomMetric(identifier: String? = null): Metric {
        val ident = identifier ?: randomIdentifier()
        val prev = random.nextDouble(200.0, 4000.0)
        val cur = prev + random.nextDouble(800.0)
        val tariff = random.nextDouble(80.0)
        return Metric(
            ident,
            random.nextDouble(),
            prev,
            cur,
            tariff,
            MeterType.random()
        )
    }

    private fun randomListOFMetrics(number: Int = 3): List<Metric> {
        return mutableListOf<Metric>().apply {
            for (i in 0..number)
                add(randomMetric())
        }
    }

    fun meterByIdentifier(identifier: String): ApiResult<Metric> {
        return ApiResult.Success<Metric>(randomMetric(identifier))
    }

    fun metersByAccountIdentifier(accountIdentifier: String): ApiResult<List<Metric>> {
        return ApiResult.Success<List<Metric>>(randomListOFMetrics())
    }

    fun metersSavedByUser(): ApiResult<List<Metric>> {
        return ApiResult.Success<List<Metric>>(randomListOFMetrics())
    }

    fun updateMeterData(currentMetric: CurrentMetric): ApiResult<*> {
        return ApiResult.Success<Any>(Any())
    }

    fun saveMeter(identifier: String): ApiResult<*> {
        return ApiResult.Success<Any>(Any())
    }

    fun deleteMeter(identifier: String): ApiResult<*> {
        return ApiResult.Success<Any>(Any())
    }
}