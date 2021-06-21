package org.vsu.pt.team2.utilitatemmetrisapp.ui.components

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import org.vsu.pt.team2.utilitatemmetrisapp.R

class AvailableOnFullAccountDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.available_only_on_full_account))
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                this.dismiss()
            }
            .create()
}