package org.vsu.pt.team2.utilitatemmetrisapp.ui.components.baseFragments

import androidx.fragment.app.Fragment
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.hideKeyboard

open class BaseFragment : Fragment() {
    override fun onStop() {
        super.onStop()
        hideKeyboard()
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }
}