package org.vsu.pt.team2.utilitatemmetrisapp.di.components

import dagger.Subcomponent
import org.vsu.pt.team2.utilitatemmetrisapp.ui.login.DemoUserFragment
import org.vsu.pt.team2.utilitatemmetrisapp.ui.login.LoginFragment

@Subcomponent
interface AuthComponent {

    fun injectLoginFragment(loginFragment: LoginFragment)

    fun injectDemoUserFragment(demoUserFragment: DemoUserFragment)

}
