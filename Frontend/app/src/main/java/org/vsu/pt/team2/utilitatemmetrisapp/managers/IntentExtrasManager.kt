package org.vsu.pt.team2.utilitatemmetrisapp.managers

import android.content.Intent

object IntentExtrasManager {
    val continueRegister = IIntentPackager.createSimple(
        { it.putExtra("ContinueRegister", true) },
        { it.getBooleanExtra("ContinueRegister", false) }
    )


    interface IIntentPackager<T> {
        fun putInto(intent: Intent)
        fun getFrom(intent: Intent): T

        companion object {
            fun <T> createSimple(
                putInto: (Intent) -> Unit,
                getFrom: (Intent) -> T,
            ): IIntentPackager<T> {
                return object : IIntentPackager<T> {
                    override fun putInto(intent: Intent) = putInto.invoke(intent)

                    override fun getFrom(intent: Intent): T = getFrom.invoke(intent)
                }
            }
        }
    }
}
