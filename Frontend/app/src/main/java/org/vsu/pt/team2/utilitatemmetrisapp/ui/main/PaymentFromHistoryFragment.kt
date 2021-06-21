package org.vsu.pt.team2.utilitatemmetrisapp.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import org.vsu.pt.team2.utilitatemmetrisapp.R
import org.vsu.pt.team2.utilitatemmetrisapp.api.model.Payment
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.FragmentPaymentFromHistoryBinding
import org.vsu.pt.team2.utilitatemmetrisapp.models.PaymentData
import org.vsu.pt.team2.utilitatemmetrisapp.repository.PaymentRepo
import org.vsu.pt.team2.utilitatemmetrisapp.ui.adapters.metersList.MetersHistoryAdapter
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.baseFragments.DisabledDrawerFragment
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.CreationFragmentArgs
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.myApplication
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.replaceFragment
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.showToast
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.GeneralButtonViewModel
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.HistoryMeterItemViewModel
import javax.inject.Inject

class PaymentFromHistoryFragment :
    DisabledDrawerFragment(R.string.fragment_title_payment_from_history) {
    private lateinit var binding: FragmentPaymentFromHistoryBinding

    private val adapter = MetersHistoryAdapter()

    private val paymentId: Int by creationFragmentArgs.asProperty()
    private lateinit var paymentData: PaymentData

    @Inject
    lateinit var paymentRepo: PaymentRepo

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentFromHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFields()
    }

    private fun onPaymentReady(payment: PaymentData) {
        val paymentItemList: List<HistoryMeterItemViewModel> =
            payment.metricDatas
                .map { pMeterData ->
                    HistoryMeterItemViewModel(
                        payment.id,
                        pMeterData.meter.identifier,
                        pMeterData.meter.type,
                        pMeterData.cost,
                        payment.date,
                        pMeterData.meter.address,
                        payment.email
                    )
                }
        adapter.submitList(paymentItemList)

        binding.paymentFromHistorySendToEmailGeneralButton.viewmodel = GeneralButtonViewModel(
            "Отправить на почту", {
                //todo send to email
            }
        )
    }

    fun initFields() {
        binding.paymentFromHistoryMetersListRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.paymentFromHistoryMetersListRecyclerView.adapter = adapter
        lifecycleScope.launchWhenCreated {
            val p = paymentRepo.findPayment(paymentId)
            if (p == null) {
                showToast(getString(R.string.err_while_loading_payment))
                parentFragmentManager.popBackStack()
                return@launchWhenCreated
            } else {
                paymentData = p
                onPaymentReady(p)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myApplication()?.appComponent?.paymentComponent()?.injectPaymentFromHistoryFragment(this)
    }

    companion object {
        val creationFragmentArgs = CreationFragmentArgs<Int>(
            { paymentDataId, bundle ->
                bundle.apply {
                    putInt("payment_id", paymentDataId)
                }
            },
            { b ->
                b.getInt("payment_id", -1)
            }
        )

        fun createWithPaymentId(paymentId: Int): PaymentFromHistoryFragment {
            return creationFragmentArgs.fill(PaymentFromHistoryFragment(), paymentId)
        }
    }
}