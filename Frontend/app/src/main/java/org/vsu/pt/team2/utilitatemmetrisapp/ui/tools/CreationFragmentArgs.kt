package org.vsu.pt.team2.utilitatemmetrisapp.ui.tools

import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class CreationFragmentArgs<Data>(
    val pack: (Data, Bundle) -> Bundle,
    val unpack: (Bundle) -> Data?
) {

    fun <Frag : Fragment> fill(fragment: Frag, data: Data): Frag {
        val b = pack.invoke(data, Bundle())
        fragment.arguments = b
        return fragment
    }

    fun asProperty(): ReadWriteProperty<Fragment, Data> {
        return object : ReadWriteProperty<Fragment, Data> {
            var value: Data? = null
            override fun setValue(thisRef: Fragment, property: KProperty<*>, value: Data) {
                this.value = value
            }

            override fun getValue(thisRef: Fragment, property: KProperty<*>): Data {
                if (this.value == null) {
                    thisRef.arguments?.let {
                        this.value = unpack.invoke(it)
                    }
                }

                return value ?: throw NullPointerException("Property ${property.name} is null. $this ")
            }
        }
    }
}