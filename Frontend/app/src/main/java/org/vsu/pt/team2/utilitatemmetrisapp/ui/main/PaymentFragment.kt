package org.vsu.pt.team2.utilitatemmetrisapp.ui.main

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.orhanobut.logger.Logger
import kotlinx.coroutines.cancel
import org.vsu.pt.team2.utilitatemmetrisapp.R
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.FragmentDialogPayBinding
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.FragmentPaymentBinding
import org.vsu.pt.team2.utilitatemmetrisapp.managers.MeterManager
import org.vsu.pt.team2.utilitatemmetrisapp.managers.PaymentManager
import org.vsu.pt.team2.utilitatemmetrisapp.models.Meter
import org.vsu.pt.team2.utilitatemmetrisapp.network.ApiResult
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.baseFragments.DisabledDrawerFragment
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.CreationFragmentArgs
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.internetConnectionLostToast
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.myApplication
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.GeneralButtonViewModel
import javax.inject.Inject

class PaymentFragment : DisabledDrawerFragment(R.string.fragment_title_payment) {
    private lateinit var binding: FragmentPaymentBinding

    private val metersIdentifiers: List<String> by PaymentFragment.creationFragmentArgs.asProperty()
    private var meters: List<Meter>? = null
    private var sum: Double = 0.0

    @Inject
    lateinit var meterManager: MeterManager

    @Inject
    lateinit var paymentManager: PaymentManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFields(binding)
        loadMeters()
    }

    private fun loadMeters() {
        lifecycleScope.launchWhenCreated {
            val loadedMeters = metersIdentifiers
                .map {
                    meterManager.getMeterByIdentifier(it)
                }
                .map {
                    when (it) {
                        is ApiResult.Success -> {
                            if (it.value.first.balance > 0.0000000001) {
                                Logger.e("PaymentFragment couldn't initialize. Loaded fragment has no backlog(balance was > 0). $it")
                                parentFragmentManager.popBackStack()
                                this.cancel()
                                return@launchWhenCreated
                            }
                            sum -= it.value.first.balance
                            it.value.first
                        }
                        else -> {
                            Logger.e("PaymentFragment couldn't initialize. Cant load all meters by identifiers. $it")
                            parentFragmentManager.popBackStack()
                            this.cancel()
                            return@launchWhenCreated
                        }
                    }
                }
            meters = loadedMeters
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myApplication()?.appComponent?.paymentComponent()?.injectPaymentFragment(this)
    }


    fun initFields(binding: FragmentPaymentBinding) {
        binding.paymentSentToEmail.generalButton.isEnabled = false
        binding.paymentSentToEmail.generalButton.visibility = View.GONE
        binding.paymentSentToEmail.viewmodel = GeneralButtonViewModel(
            getString(R.string.sent_to_email), {
                //todo sent to email
            }
        )
        binding.paymentPay.viewmodel = GeneralButtonViewModel(
            getString(R.string.to_pay), {
                PaymentDialog(sum).show(parentFragmentManager, "PaymentDialogFragment")
            }
        )
    }

    private suspend fun requestOnDoPaymentClick() {
        val paymentResult = paymentManager.doPayment(metersIdentifiers, sum, metersIdentifiers[0])
        when(paymentResult){
            is ApiResult.Success->{
                onSuccessPay()
            }
            is ApiResult.GenericError->{
                //todo showtoast
            }
            is ApiResult.NetworkError->{
                internetConnectionLostToast()
            }
        }
    }

    private fun onSuccessPay() {
        binding.paymentSentToEmail.generalButton.isEnabled = true
        binding.paymentSentToEmail.generalButton.visibility = View.VISIBLE

        binding.paymentPay.generalButton.isEnabled = false
        binding.paymentPay.generalButton.visibility = View.GONE
    }

    private fun onCancelPay() {

    }

    class PaymentDialog(
        private val sumForPay: Double
    ) : DialogFragment() {

        private lateinit var binding: FragmentDialogPayBinding

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            if (sumForPay.compareTo(0.0000000000001) <= 0)
                dismiss()
            dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corner)
            binding = FragmentDialogPayBinding.inflate(
                inflater,
                container,
                false
            )

            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            binding.fragmentDialogPaySumTv.text = sumForPay.toString()
            binding.fragmentDialogPayBtnPay.setOnClickListener {
                //todo
            }
        }

        override fun onStart() {
            super.onStart()
            val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
            val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
            dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        }

        override fun onCancel(dialog: DialogInterface) {
            super.onCancel(dialog)
        }

        override fun onDismiss(dialog: DialogInterface) {
            super.onDismiss(dialog)
        }

        override fun onDestroy() {
            super.onDestroy()
        }


    }

    companion object {
        val creationFragmentArgs = CreationFragmentArgs<List<String>>(
            { identifiers, bundle ->
                bundle.putStringArray("Identifiers", identifiers.toTypedArray())
                bundle
            },
            { bundle ->
                bundle.getStringArray("Identifiers")?.asList()
            }
        )

        fun createWithMetersIdentifier(identifiers: List<String>): PaymentFragment {
            return creationFragmentArgs.fill(PaymentFragment(), identifiers)
        }
    }
}