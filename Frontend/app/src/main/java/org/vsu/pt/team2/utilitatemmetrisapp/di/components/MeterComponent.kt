package org.vsu.pt.team2.utilitatemmetrisapp.di.components

import dagger.Subcomponent
import org.vsu.pt.team2.utilitatemmetrisapp.ui.main.AddMeterFragment
import org.vsu.pt.team2.utilitatemmetrisapp.ui.main.MeterFragment
import org.vsu.pt.team2.utilitatemmetrisapp.ui.main.SavedMetersFragment


@Subcomponent(modules = [])
interface MeterComponent {

    fun injectSavedMetersFragment(savedMetersFragment: SavedMetersFragment)

    fun injectMeterFragment(meterFragment: MeterFragment)

    fun injectAddMeterFragment(addMeterFragment: AddMeterFragment)
}