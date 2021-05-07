package org.vsu.pt.team2.utilitatemmetrisapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import org.vsu.pt.team2.utilitatemmetrisapp.R
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.FragmentAccountBinding
import org.vsu.pt.team2.utilitatemmetrisapp.managers.BundleManager.AccountViewModelBundlePackager
import org.vsu.pt.team2.utilitatemmetrisapp.models.MeterType
import org.vsu.pt.team2.utilitatemmetrisapp.ui.adapters.metersList.MetersWithCheckboxListAdapter
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.baseFragments.BaseTitledFragment
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.baseFragments.DisabledDrawerFragment
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.appCompatActivity
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.replaceFragment
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.GeneralButtonViewModel
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.MeterViewModel

class AccountFragment : DisabledDrawerFragment() {

    private val adapter = MetersWithCheckboxListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAccountBinding.inflate(inflater, container, false)
        initFields(binding)
        arguments?.let {
            AccountViewModelBundlePackager.getFrom(it)?.let {
                binding.accountAddress = it.address
                //todo Счёт в strings
                title = "Счёт " + it.identifier
            }
        }
        updateAdapter()
        return binding.root
    }

    fun initFields(binding : FragmentAccountBinding){
        binding.sumForPay = 1
        binding.payChosenMetersButton.viewmodel =
            GeneralButtonViewModel(getString(R.string.pay_for_chosen)) {
                //todo action when pay for all
                appCompatActivity()?.replaceFragment(PaymentFragment())
            }
        binding.metersOnAccountRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding.metersOnAccountRecyclerView.adapter = adapter
    }

    fun updateAdapter() {
        val list = mutableListOf<MeterViewModel>()
        list.add(
            MeterViewModel(
                "7a6d87asd", MeterType.ColdWater, 452.4
            )
        )
        list.add(
            MeterViewModel(
                "6633pqff445", MeterType.Elect, 1209.1
            )
        )


        adapter.submitList(list)
    }
}