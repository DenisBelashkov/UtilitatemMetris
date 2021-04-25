package org.vsu.pt.team2.utilitatemmetrisapp.ui.components

import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.appCompatActivity
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.mainActivity

open class DisabledDrawerFragment : BaseFragment() {
    override fun onStart() {
        super.onStart()
        mainActivity()?.drawerController?.disableDrawer()
    }

    override fun onStop() {
        super.onStop()
        mainActivity()?.drawerController?.enableDrawer()
    }
}