package org.vsu.pt.team2.utilitatemmetrisapp.managers

import android.os.Bundle
import org.vsu.pt.team2.utilitatemmetrisapp.models.Meter
import org.vsu.pt.team2.utilitatemmetrisapp.models.MeterType
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.AccountViewModel
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.MeterViewModel
import java.util.logging.Logger

object BundleManager {

    private val logger = Logger.getLogger(BundleManager::class.java.simpleName)
    val AccountViewModelBundlePackager = IBundlePackager.createSimple<AccountViewModel>(
        { bundle, accVM ->
            bundle.apply {
                putString("Address", accVM.address)
                putString("Identifier", accVM.identifier)
            }
        },
        {
            val address = it.getString("Address")
            val ident = it.getString("Identifier")
            if (address == null || ident == null)
                null
            else
                AccountViewModel(ident, address)
        }
    )

    val MeterBundlePackager = object : BundleManager.IBundlePackager<Meter> {
        override fun putInto(bundle: Bundle, meter: Meter) {
            bundle.apply {
                putString("Identifier", meter.identifier)
                putDouble("Balance", meter.balance)
                putDouble("CurMonthData", meter.curMonthData)
                putDouble("PrevMonthData", meter.prevMonthData)
                putDouble("Tariff", meter.tariff)
                putString("MeterType", meter.type.name)
                putBoolean("IsSaved", meter.isSaved)
            }
        }

        override fun getFrom(bundle: Bundle): Meter? {
            bundle.apply {
                val id = getString("Identifier").also {
                    if (it == null)
                        logger.log(java.util.logging.Level.SEVERE, "bundle contains no identifier")
                } ?: return null
                val meterTypeString = getString("MeterType").also {
                    if (it == null)
                        logger.log(java.util.logging.Level.SEVERE, "bundle contains no MeterType")
                } ?: return null
                val meterType = MeterType.valueOf(meterTypeString)
                val tariff = getDouble("Tariff").also {
                    if (it == 0.0) {
                        logger.log(
                            java.util.logging.Level.SEVERE,
                            "bundle contains zero tariff"
                        )
                        return null
                    }
                }
                val curMonthData = getDouble("CurMonthData").also {
                    if (it == 0.0) {
                        logger.log(
                            java.util.logging.Level.SEVERE,
                            "bundle contains zero curMonthData"
                        )
                        return null
                    }
                }
                val prevMonthData = getDouble("PrevMonthData").also {
                    if (it == 0.0) {
                        logger.log(
                            java.util.logging.Level.SEVERE,
                            "bundle contains zero prevMonthData"
                        )
                        return null
                    }
                }
                val balance = getDouble("Balance")
                val isSaved = getBoolean("IsSaved", false)
                return Meter(
                    id,
                    meterType,
                    tariff,
                    prevMonthData,
                    curMonthData,
                    balance,
                    isSaved,
                )
            }
        }

    }


    interface IBundlePackager<T> {
        fun putInto(bundle: Bundle, value: T)
        fun getFrom(bundle: Bundle): T?

        companion object {
            fun <T> createSimple(
                putInto: (Bundle, value: T) -> Unit,
                getFrom: (Bundle) -> T?,
            ): IBundlePackager<T> {
                return object : IBundlePackager<T> {
                    override fun putInto(bundle: Bundle, value: T) = putInto.invoke(bundle, value)

                    override fun getFrom(bundle: Bundle): T? = getFrom.invoke(bundle)
                }
            }
        }
    }
}
