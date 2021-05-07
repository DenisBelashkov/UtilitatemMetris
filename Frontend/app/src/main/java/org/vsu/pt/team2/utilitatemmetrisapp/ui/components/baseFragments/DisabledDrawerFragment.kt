package org.vsu.pt.team2.utilitatemmetrisapp.ui.components.baseFragments

import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.mainActivity

open class DisabledDrawerFragment(titleResId: Int? = null) : BaseTitledFragment(titleResId) {
    override fun onStart() {
        super.onStart()
        mainActivity()?.drawerController?.disableDrawer()
    }

    override fun onStop() {
        super.onStop()
        mainActivity()?.drawerController?.enableDrawer()
    }
}