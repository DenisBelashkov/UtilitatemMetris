package org.vsu.pt.team2.utilitatemmetrisapp.managers

import android.os.Bundle
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.AccountViewModel

object BundleManager {
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
