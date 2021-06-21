package org.vsu.pt.team2.utilitatemmetrisapp.ui

import android.content.Context
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.*
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.AccountViewModel
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.HistoryMeterItemViewModel
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.MeterItemViewModel
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.MeterViewModel
import kotlin.math.abs


fun ItemMeterBinding.setFromVM(mvm: MeterItemViewModel, context: Context) {
    setBacklogValue(mvm.backlog)
    hasBacklog = mvm.backlog > 0.0000001
    setMeterIdentifier(mvm.identifier)
    meterType = mvm.type.toLanguagedString(context)

    shouldShowLabels = true
}

fun ItemMeterWithCheckboxBinding.setFromVM(mvm: MeterItemViewModel, context: Context) {
    setBacklogValue(mvm.backlog)
    hasBacklog = mvm.backlog > 0.0000001
    setMeterIdentifier(mvm.identifier)
    meterType = mvm.type.toLanguagedString(context)
    address = mvm.address.substringBefore("город")

    shouldShowLabels = true
}

fun ItemMeterHistoryBinding.setFromVM(hmvm: HistoryMeterItemViewModel, context: Context) {
    setMeterIdentifier(hmvm.identifier)
    meterType = hmvm.type.toLanguagedString(context)
    sum = hmvm.sum
    date = hmvm.date
    address = hmvm.address

    shouldShowLabels = false
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