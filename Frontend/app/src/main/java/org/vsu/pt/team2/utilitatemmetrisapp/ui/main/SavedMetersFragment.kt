package org.vsu.pt.team2.utilitatemmetrisapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.vsu.pt.team2.utilitatemmetrisapp.R
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.FragmentSavedMetersBinding
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.BaseFragment
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.GeneralButtonViewModel

class SavedMetersFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSavedMetersBinding.inflate(inflater, container, false)
        binding.sumForPay = 1
        binding.payForChosenButton.viewmodel =
            GeneralButtonViewModel(getString(R.string.pay_for_chosen)) {
                //todo action when pay for all
            }
        return binding.root
    }
}