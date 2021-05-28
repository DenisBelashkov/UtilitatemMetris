package org.vsu.pt.team2.utilitatemmetrisapp.di.components

import dagger.Subcomponent
import org.vsu.pt.team2.utilitatemmetrisapp.ui.main.AccountFragment
import org.vsu.pt.team2.utilitatemmetrisapp.ui.main.MyAccountsFragment

@Subcomponent
interface AccountComponent {

    fun injectAccountFragment(accountFragment: AccountFragment)

    fun injectMyAccountsFragment(myAccountsFragment: MyAccountsFragment)
}
