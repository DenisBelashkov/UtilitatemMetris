package org.vsu.pt.team2.utilitatemmetrisapp.ui.components.baseFragments

import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.appCompatActivity
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.setTitleIfMain

open class BaseTitledFragment(private val titleStringResId: Int? = null) : BaseFragment() {
    var title: String = ""
    override fun onStart() {
        super.onStart()
        if (title.isBlank() && titleStringResId != null)
            title = requireContext().getString(titleStringResId)
        appCompatActivity()?.setTitleIfMain(title)
    }

    override fun onStop() {
        super.onStop()
        appCompatActivity()?.setTitleIfMain("")
    }
}