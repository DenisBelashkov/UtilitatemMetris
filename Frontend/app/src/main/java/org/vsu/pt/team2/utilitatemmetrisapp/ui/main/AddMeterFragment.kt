package org.vsu.pt.team2.utilitatemmetrisapp.ui.main

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.vsu.pt.team2.utilitatemmetrisapp.R
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.FragmentAddMeterBinding
import org.vsu.pt.team2.utilitatemmetrisapp.managers.MeterManager
import org.vsu.pt.team2.utilitatemmetrisapp.models.Meter
import org.vsu.pt.team2.utilitatemmetrisapp.network.ApiResult
import org.vsu.pt.team2.utilitatemmetrisapp.repository.SavedMeterRepo
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.baseFragments.BaseTitledFragment
import org.vsu.pt.team2.utilitatemmetrisapp.ui.setFromVM
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.replaceFragment
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.requireAppCompatActivity
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.requireMyApplication
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.MeterItemViewModel
import javax.inject.Inject

class AddMeterFragment : BaseTitledFragment(R.string.fragment_title_add_meter) {
    private lateinit var binding: FragmentAddMeterBinding

    @Inject
    lateinit var meterManager: MeterManager

    @Inject
    lateinit var savedMeterRepo: SavedMeterRepo

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddMeterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initFields(binding)
    }

    private fun initFields(binding: FragmentAddMeterBinding) {
        binding.meterFound = false
        binding.meterIdentifierExtendededittext.addTextChangedListener(identifierTextChangedListener)
    }

    fun loadMeters(correctMeterIdentifier: String) {
        lifecycleScope.launch {
            val meter: Meter?
            val res = meterManager.loadMeter(correctMeterIdentifier)
            when (res) {
                is ApiResult.Success ->
                    meter = res.value
                is ApiResult.GenericError, ApiResult.NetworkError ->
                    meter = savedMeterRepo.meterOrNull(correctMeterIdentifier)?.also {
                        //todo показать Toast с надписью "загружено из локального репозитория"
                    }
            }
            meter?.let {
                binding.meterFound = true
                binding.meterContent.setFromVM(
                    MeterItemViewModel(
                        it.identifier,
                        it.type,
                        it.balance
                    ),
                    requireContext()
                )
                binding.meterContent.root.setOnClickListener {
                    val meterFragment = MeterFragment.createWithMeterIdentifier(meter.identifier)
                    replaceFragment(meterFragment)
                }
            } ?: run {
                binding.meterFound = false
            }

        }
    }

    private val identifierTextChangedListener = object : TextWatcher {
        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {
            //empty
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            //empty
        }

        override fun afterTextChanged(s: Editable?) {
            //todo Проверка на правильность идентификатора
            s?.let {
                if (it.length >= 6) {
                    loadMeters(it.toString())
                } else
                    binding.meterFound = false
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireAppCompatActivity().requireMyApplication().appComponent?.meterComponent()
            ?.injectAddMeterFragment(this)
    }
}