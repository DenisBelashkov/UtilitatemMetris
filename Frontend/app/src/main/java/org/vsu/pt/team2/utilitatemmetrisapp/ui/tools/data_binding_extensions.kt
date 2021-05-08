package org.vsu.pt.team2.utilitatemmetrisapp.ui

import android.content.Context
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.FragmentMeterBinding
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.ItemAccountBinding
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.ItemMeterBinding
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.ItemMeterWithCheckboxBinding
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.AccountViewModel
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.MeterItemViewModel
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.MeterViewModel


fun ItemMeterBinding.setFromVM(mvm: MeterItemViewModel, context: Context) {
    setBacklogValue(mvm.backlog)
    setMeterIdentifier(mvm.identifier)
    meterType = mvm.type.toLanguagedString(context)
}

fun ItemMeterWithCheckboxBinding.setFromVM(mvm: MeterItemViewModel, context: Context) {
    setBacklogValue(mvm.backlog)
    setMeterIdentifier(mvm.identifier)
    meterType = mvm.type.toLanguagedString(context)
}

fun ItemAccountBinding.setFromVM(accountViewModel: AccountViewModel) {
    address = accountViewModel.address
    identifier = accountViewModel.identifier
}

fun FragmentMeterBinding.setFromVM(meterViewModel: MeterViewModel, context: Context) {
    identifier = meterViewModel.identifier
    backlog = meterViewModel.backlog
    prevMonthData = meterViewModel.prevMonthData
    tariff = meterViewModel.tariff
    type = meterViewModel.type.toLanguagedString(context)
}