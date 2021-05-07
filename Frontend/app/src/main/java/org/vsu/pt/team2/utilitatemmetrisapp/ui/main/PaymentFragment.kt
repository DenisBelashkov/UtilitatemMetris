package org.vsu.pt.team2.utilitatemmetrisapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.vsu.pt.team2.utilitatemmetrisapp.R
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.FragmentPaymentBinding
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.baseFragments.DisabledDrawerFragment
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.GeneralButtonViewModel

class PaymentFragment : DisabledDrawerFragment(R.string.fragment_title_payment) {
    private lateinit var binding: FragmentPaymentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentBinding.inflate(inflater, container, false)
        initFields(binding)
        return binding.root
    }

    fun initFields(binding: FragmentPaymentBinding) {
        binding.paymentSentToEmail.viewmodel = GeneralButtonViewModel(
            getString(R.string.sent_to_email), {
                //todo sent to email
            }
        )
        binding.paymentPay.viewmodel = GeneralButtonViewModel(
            getString(R.string.to_pay), {
                //todo pay
            }
        )
    }
}