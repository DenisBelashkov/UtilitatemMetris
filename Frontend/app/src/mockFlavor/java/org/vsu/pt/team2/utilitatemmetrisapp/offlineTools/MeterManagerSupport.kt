package org.vsu.pt.team2.utilitatemmetrisapp.offlineTools

import org.vsu.pt.team2.utilitatemmetrisapp.api.model.CurrentMetric
import org.vsu.pt.team2.utilitatemmetrisapp.api.model.Metric
import org.vsu.pt.team2.utilitatemmetrisapp.models.MeterType
import org.vsu.pt.team2.utilitatemmetrisapp.network.ApiResult
import javax.inject.Inject
import kotlin.math.pow
import kotlin.math.roundToInt

class MeterManagerSupport @Inject constructor(
    private val random: RandomTools
) {

    fun randomMetric(identifier: String? = null): Metric {
        fun Double.roundTo(numFractionDigits: Int): Double {
            val factor = 10.0.pow(numFractionDigits.toDouble())
            return (this * factor).roundToInt() / factor
        }

        val ident = identifier ?: random.randomMeterIdentifier()
        val prev = random.nextDouble(200.0, 4000.0)
        val balance =
            if (random.nextInt(3) == 0)
                0.0
            else
                -random.nextDouble(2000.0)
        val cur = prev + random.nextDouble(800.0)
        val tariff = random.nextDouble(80.0)
        return Metric(
            ident,
            balance.roundTo(2),
            prev.roundTo(2),
            cur.roundTo(2),
            tariff.roundTo(2),
            MeterType.random()
        )
    }

    private fun randomListOFMetrics(number: Int = 3): List<Metric> {
        return mutableListOf<Metric>().apply {
            for (i in 0..number)
                add(randomMetric())
        }
    }

    fun randomMeterByIdentifierWithRandomSavedOrNot(identifier: String): ApiResult<Pair<Metric, Boolean>> {
        return ApiResult.Success<Pair<Metric, Boolean>>(
            Pair(
                randomMetric(identifier),
                random.nextBoolean()
            )
        )
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