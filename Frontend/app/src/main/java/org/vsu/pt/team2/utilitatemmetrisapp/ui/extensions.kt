package org.vsu.pt.team2.utilitatemmetrisapp.ui

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.ItemAccountBinding
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.ItemMeterBinding
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.ItemMeterWithCheckboxBinding
import org.vsu.pt.team2.utilitatemmetrisapp.ui.main.MainActivity
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.AccountViewModel
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.MeterViewModel

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun ItemMeterBinding.setFromVM(mvm: MeterViewModel, context: Context) {
    setBacklogValue(mvm.backlog)
    setMeterIdentifier(mvm.identifier)
    meterType = mvm.type.toLanguagedString(context)
}

fun ItemMeterWithCheckboxBinding.setFromVM(mvm: MeterViewModel, context: Context) {
    setBacklogValue(mvm.backlog)
    setMeterIdentifier(mvm.identifier)
    meterType = mvm.type.toLanguagedString(context)
}

fun ItemAccountBinding.setFromVM(accountViewModel: AccountViewModel) {
    address = accountViewModel.address
    identifier = accountViewModel.identifier
}

fun AppCompatActivity.setTitleIfMain(title: String) {
    (this as? MainActivity)?.titleTV?.text = title
}