package org.vsu.pt.team2.utilitatemmetrisapp.ui.components.baseFragments

import androidx.fragment.app.Fragment
import org.vsu.pt.team2.utilitatemmetrisapp.ui.hideKeyboard
import org.vsu.pt.team2.utilitatemmetrisapp.ui.setTitleIfMain
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.appCompatActivity

open class BaseTitledFragment(private val titleStringResId: Int? = null) : Fragment() {
    var title: String = ""
    override fun onStart() {
        super.onStart()
        if (title.isBlank() && titleStringResId != null)
            title = requireContext().getString(titleStringResId)
        appCompatActivity()?.setTitleIfMain(title)
    }

    override fun onStop() {
        super.onStop()
        hideKeyboard()
        appCompatActivity()?.setTitleIfMain("")
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }
}