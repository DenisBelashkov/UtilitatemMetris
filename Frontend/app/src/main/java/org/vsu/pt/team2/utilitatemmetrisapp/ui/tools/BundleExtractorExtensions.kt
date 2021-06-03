package org.vsu.pt.team2.utilitatemmetrisapp.ui.tools

import androidx.fragment.app.Fragment
import org.vsu.pt.team2.utilitatemmetrisapp.managers.BundleManager
import java.lang.NullPointerException
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T> Fragment.bundleArgs(
    packager : BundleManager.IBundlePackager<T>,
    popBackStackOnNull: Boolean = false,
): ReadWriteProperty<Fragment, T> {
    return object : ReadWriteProperty<Fragment, T> {
        var value: T? = null
        override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
            this.value = value
        }

        override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
            if (this.value == null) {
                thisRef.arguments?.let {
                    this.value = packager.getFrom(it)
                }
            }

            if (popBackStackOnNull && value == null)
                parentFragmentManager.popBackStack()

            return value ?: throw NullPointerException("Property is null. $this")
        }

    }
}