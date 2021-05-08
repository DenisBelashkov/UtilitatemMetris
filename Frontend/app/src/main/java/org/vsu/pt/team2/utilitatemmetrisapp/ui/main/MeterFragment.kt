package org.vsu.pt.team2.utilitatemmetrisapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.vsu.pt.team2.utilitatemmetrisapp.R
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.FragmentMeterBinding
import org.vsu.pt.team2.utilitatemmetrisapp.managers.BundleManager
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.baseFragments.DisabledDrawerFragment
import org.vsu.pt.team2.utilitatemmetrisapp.ui.setFromVM
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.GeneralButtonViewModel
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.MeterViewModel

class MeterFragment : DisabledDrawerFragment(R.string.fragment_title_meter) {
    private lateinit var binding: FragmentMeterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMeterBinding.inflate(inflater, container, false)
        initFields(binding)
        return binding.root
    }

    fun initFields(binding: FragmentMeterBinding) {
        arguments?.let { bundle ->
            BundleManager.MeterViewModelBundlePackager.getFrom(bundle)
                ?.let {
                    binding.setFromVM(it, requireContext())
                } ?: parentFragmentManager.popBackStack()
        }

        binding.meterShowHistory.viewmodel = GeneralButtonViewModel(
            getString(R.string.show_payment_history),
            {
                //todo показать историю платежей
            }
        )
        binding.meterPayBacklog.viewmodel = GeneralButtonViewModel(
            getString(R.string.pay_backlog),
            {
                //todo погасить задолженность
            }
        )
    }

    companion object {
        fun createWithVM(mvm: MeterViewModel): MeterFragment {
            val b = Bundle()
            BundleManager.MeterViewModelBundlePackager.putInto(b, mvm)
            val f = MeterFragment()
            f.arguments = b
            return f
        }
    }
}