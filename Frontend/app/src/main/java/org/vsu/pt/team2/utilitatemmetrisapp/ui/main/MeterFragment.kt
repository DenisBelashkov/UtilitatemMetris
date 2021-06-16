package org.vsu.pt.team2.utilitatemmetrisapp.ui.main

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.orhanobut.logger.Logger
import kotlinx.coroutines.launch
import org.vsu.pt.team2.utilitatemmetrisapp.R
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.FragmentDialogAcceptChangesBinding
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.FragmentMeterBinding
import org.vsu.pt.team2.utilitatemmetrisapp.managers.MeterManager
import org.vsu.pt.team2.utilitatemmetrisapp.models.Meter
import org.vsu.pt.team2.utilitatemmetrisapp.network.ApiResult
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.baseFragments.DisabledDrawerFragment
import org.vsu.pt.team2.utilitatemmetrisapp.ui.setFromVM
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.*
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.GeneralButtonViewModel
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.MeterViewModel
import javax.inject.Inject


class MeterFragment : DisabledDrawerFragment(R.string.fragment_title_meter) {
    private lateinit var binding: FragmentMeterBinding
    private val meterIdentifier by MeterFragment.creationFragmentArgs.asProperty()
    private var meter: Meter? = null
    private var isSaved: Boolean = false
    private var menu: Menu? = null

    @Inject
    lateinit var meterManager: MeterManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMeterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initFields(binding)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.meter_menu, menu)
        this.menu = menu
        this.menu?.findItem(R.id.meter_menu_fav)?.let {
            setMenuItemState(isSaved, it)
        }
    }

    private fun setMenuItemState(isMeterSaved: Boolean, item: MenuItem) {
        item.isChecked = isMeterSaved

        if (isMeterSaved)
            item.setIcon(R.drawable.ic_star_filled_24)
        else
            item.setIcon(R.drawable.ic_star_outline_24)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.meter_menu_fav -> {
                val changedValue = !item.isChecked

                lifecycleScope.launch {
                    val success = changeFav(changedValue)


                    if (success) {
                        setMenuItemState(changedValue, item)
                    }
//                        item.isChecked = changedValue
//
//
//                    if (item.isChecked)
//                        item.setIcon(R.drawable.ic_star_filled_24)
//                    else
//                        item.setIcon(R.drawable.ic_star_outline_24)
                }
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
    }

    /**
     * @param isFav value to set
     * @return is successfully changed
     */
    suspend fun changeFav(isFav: Boolean): Boolean {
        meter?.let {
            val res = if (isFav)
                meterManager.saveMeter(it.identifier)
            else
                meterManager.deleteMeter(it.identifier)
            return when (res) {
                is ApiResult.Success ->
                    true
                is ApiResult.GenericError -> {
                    genericErrorToast(res)
                    false
                }
                is ApiResult.NetworkError -> {
                    networkConnectionErrorToast()
                    false
                }
            }
        }
        return false
    }

    fun initFields(binding: FragmentMeterBinding) {
        binding.fragmentMeterNewdataTextfieldboxes.endIconImageButton.setOnClickListener {
            val newData = binding.fragmentMeterNewdataExtendededittext
                .text
                .toString()
                .toDoubleOrNull()
            if (newData == null)
                Logger.e("Cant convert new data value to double")
            else {
                Logger.d(
                    "apply new data clicked, new data: " +
                            binding.fragmentMeterNewdataExtendededittext.text.toString()
                )
                meter?.let { m ->
                    AcceptChangesDialog(
                        m.curMonthData,
                        newData,
                        { nv, onSuccess -> acceptChangesClicked(nv, onSuccess) }
                    )
                        .show(parentFragmentManager, "AcceptChangesDialogFragment")
                }
            }
            //dialog показать с вопросом "Сменить значения на [новые]?"
            //если юзер во временном аккаунте, отправлять на почту письмо для подтверждения
            //или отправлять раз в 4 раза
            //или спросить у сервера, "надо ли подтверждать действие?"
        }
//        arguments?.let { bundle ->
//            BundleManager.MeterViewModelBundlePackager.getFrom(bundle)
//                ?.let {
//                    binding.setFromVM(it, requireContext())
//                } ?: parentFragmentManager.popBackStack()
//        }
        lifecycleScope.launchWhenCreated {
            try {
                val res = meterManager.getMeterByIdentifier(meterIdentifier)
                when (res) {
                    is ApiResult.NetworkError -> {
                        networkConnectionErrorToast()
                        parentFragmentManager.popBackStack()
                    }
                    is ApiResult.GenericError -> {
                        genericErrorToast(res)
                        parentFragmentManager.popBackStack()
                    }
                    is ApiResult.Success -> {
                        meter = res.value.first
                        binding.setFromVM(
                            MeterViewModel.fromMeter(res.value.first),
                            requireContext()
                        )
                        isSaved = res.value.second
                        menu?.findItem(R.id.meter_menu_fav)?.let {
                            setMenuItemState(isSaved, it)
                        }
                        meter?.let { m ->
                            if (!m.curMonthData.toString().isNullOrBlank()) {
                                binding.fragmentMeterNewdataExtendededittext.setText(m.curMonthData.toString())
                            } else {
                                binding.fragmentMeterNewdataExtendededittext.setText(m.prevMonthData.toString())
                            }
                        }
                    }
                }
            } catch (npe: NullPointerException) {
                parentFragmentManager.popBackStack()
            }

        }

        binding.meterShowHistory.viewmodel = GeneralButtonViewModel(
            getString(R.string.show_payment_history),
            {
                //todo переход на экран "истории платежей"
            }
        )
        binding.meterPayBacklog.viewmodel = GeneralButtonViewModel(
            getString(R.string.pay_backlog),
            {
                PaymentFragment.createWithMetersIdentifier(
                    listOf(
                        meterIdentifier
                    )
                ).also {
                    appCompatActivity()?.replaceFragment(it)
                }
            }
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireAppCompatActivity().requireMyApplication().appComponent?.meterComponent()
            ?.injectMeterFragment(this)
    }

    private suspend fun acceptChangesRequest(newValue: Double, onSuccess: () -> Unit) {
        when (val updateResult = meterManager.updateMeterData(meterIdentifier, newValue)) {
            is ApiResult.NetworkError ->
                networkConnectionErrorToast()
            is ApiResult.GenericError -> {
                genericErrorToast(updateResult)
            }
            is ApiResult.Success -> {
                meter?.curMonthData = newValue
                binding.fragmentMeterNewdataExtendededittext.setText(newValue.toString())
                onSuccess.invoke()
            }
        }
    }

    private fun acceptChangesClicked(newValue: Double, onSuccess: () -> Unit) {
        lifecycleScope.launch {
            acceptChangesRequest(newValue, onSuccess)
        }
    }

    class AcceptChangesDialog(
        private val oldValue: Double,
        private val newValue: Double,
        private val onAcceptClick: (newValue: Double, onSuccess: () -> Unit) -> Unit
    ) : DialogFragment() {

        lateinit var binding: FragmentDialogAcceptChangesBinding

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            if (oldValue >= newValue) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.new_value_lower_than_previous),
                    Toast.LENGTH_LONG
                ).show()
                dismiss()
            }
            dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corner)
            binding = FragmentDialogAcceptChangesBinding.inflate(
                inflater,
                container,
                false
            )

            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            binding.fragmentDialogAcceptChangesCurFromData.text = oldValue.toString()
            binding.fragmentDialogAcceptChangesCurToData.text = newValue.toString()
            binding.fragmentDialogAcceptChangesBtnAccept.setOnClickListener {
                onAcceptClick.invoke(newValue,{dismiss()})
            }
            binding.fragmentDialogAcceptChangesBtnCancel.setOnClickListener {
                dismiss()
            }
        }

        override fun onStart() {
            super.onStart()
            val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
            val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
            dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    companion object {
        private val creationFragmentArgs = CreationFragmentArgs<String>(
            { meterIdentifier, bundle ->
                bundle.putString("Ident", meterIdentifier)
                bundle
            },
            { bundle ->
                bundle.getString("Ident", null)
            }
        )

        fun createWithMeterIdentifier(identifier: String): MeterFragment {
            return creationFragmentArgs.fill(MeterFragment(), identifier)
        }
    }
}