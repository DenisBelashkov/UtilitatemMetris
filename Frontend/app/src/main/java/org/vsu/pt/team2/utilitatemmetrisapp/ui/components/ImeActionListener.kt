package org.vsu.pt.team2.utilitatemmetrisapp.ui.components

import android.view.KeyEvent
import android.widget.TextView

class ImeActionListener(
    vararg actions: Association
) : TextView.OnEditorActionListener {

    private val map = mutableMapOf<Int, () -> Unit>().also { map ->
        actions.forEach { map.put(it.id, it.action) }
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        map.get(actionId)?.let {
            it.invoke()
            return true
        }
        return false
    }

    data class Association(
        val id: Int,
        val action: () -> Unit,
    )
}