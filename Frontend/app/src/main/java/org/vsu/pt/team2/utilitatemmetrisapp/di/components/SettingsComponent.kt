package org.vsu.pt.team2.utilitatemmetrisapp.di.components

import dagger.Subcomponent
import org.vsu.pt.team2.utilitatemmetrisapp.ui.main.SettingsFragment

@Subcomponent
interface SettingsComponent {

    fun inject(settingsFragment: SettingsFragment)

}
