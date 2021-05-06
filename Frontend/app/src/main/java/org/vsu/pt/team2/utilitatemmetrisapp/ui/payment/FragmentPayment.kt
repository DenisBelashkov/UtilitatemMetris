package org.vsu.pt.team2.utilitatemmetrisapp.ui.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.vsu.pt.team2.utilitatemmetrisapp.R
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.FragmentPaymentBinding
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.DisabledDrawerFragment
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.GeneralButtonViewModel

class FragmentPayment: DisabledDrawerFragment() {
    private lateinit var binding : FragmentPaymentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentBinding.inflate(inflater,container,false)
        initFields(binding)
        return binding.root
    }

    fun initFields(binding: FragmentPaymentBinding){
        binding.paymentSentToEmail.viewmodel = GeneralButtonViewModel(
            getString(R.string.sent_to_email),{
                //todo sent to email
            }
        )
        binding.paymentPay.viewmodel = GeneralButtonViewModel(
            getString(R.string.to_pay),{
                //todo pay
            }
        )
    }
}