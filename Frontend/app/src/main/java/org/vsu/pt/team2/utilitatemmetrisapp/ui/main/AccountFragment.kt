package org.vsu.pt.team2.utilitatemmetrisapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.vsu.pt.team2.utilitatemmetrisapp.R
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.FragmentAccountBinding
import org.vsu.pt.team2.utilitatemmetrisapp.managers.BundleManager.AccountViewModelBundlePackager
import org.vsu.pt.team2.utilitatemmetrisapp.managers.MeterManager
import org.vsu.pt.team2.utilitatemmetrisapp.models.MeterType
import org.vsu.pt.team2.utilitatemmetrisapp.ui.adapters.metersList.MetersWithCheckboxListAdapter
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.baseFragments.DisabledDrawerFragment
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.appCompatActivity
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.replaceFragment
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.GeneralButtonViewModel
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.MeterItemViewModel
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.MeterViewModel
import javax.inject.Inject

class AccountFragment : DisabledDrawerFragment() {

    @Inject
    lateinit var meterManager: MeterManager

    private val adapter = MetersWithCheckboxListAdapter {
        lifecycleScope.launch {
            val f = MeterFragment.createWithMeterIdentifier(it.identifier)
            replaceFragment(f)
        }

        val vm = MeterViewModel.fromMeterItemVM(
            it,
            4.86,
            3098.92,
            3124.12,
            false
        )

    }

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

    fun initFields(binding: FragmentAccountBinding) {
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
        val list = mutableListOf<MeterItemViewModel>()
        list.add(
            MeterItemViewModel(
                "7a6d87asd", MeterType.ColdWater, 452.4
            )
        )
        list.add(
            MeterItemViewModel(
                "6633pqff445", MeterType.Elect, 1209.1
            )
        )


        adapter.submitList(list)
    }
}