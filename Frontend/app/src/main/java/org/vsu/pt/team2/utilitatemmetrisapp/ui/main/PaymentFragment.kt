package org.vsu.pt.team2.utilitatemmetrisapp.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.orhanobut.logger.Logger
import kotlinx.coroutines.cancel
import org.vsu.pt.team2.utilitatemmetrisapp.R
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.FragmentPaymentBinding
import org.vsu.pt.team2.utilitatemmetrisapp.managers.MeterManager
import org.vsu.pt.team2.utilitatemmetrisapp.models.Meter
import org.vsu.pt.team2.utilitatemmetrisapp.network.ApiResult
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.baseFragments.DisabledDrawerFragment
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.CreationFragmentArgs
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.myApplication
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.GeneralButtonViewModel
import javax.inject.Inject

class PaymentFragment : DisabledDrawerFragment(R.string.fragment_title_payment) {
    private lateinit var binding: FragmentPaymentBinding

    private val metersIdentifiers: List<String> by PaymentFragment.creationFragmentArgs.asProperty()
    private var meters: List<Meter>? = null

    @Inject
    lateinit var meterManager: MeterManager

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
            val loadedMeters = metersIdentifiers.map {
                meterManager.getMeterByIdentifier(it)
            }.map {
                when (it) {
                    is ApiResult.Success ->
                        it.value.first
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
            return creationFragmentArgs.fill(PaymentFragment(),identifiers)
        }
    }
}