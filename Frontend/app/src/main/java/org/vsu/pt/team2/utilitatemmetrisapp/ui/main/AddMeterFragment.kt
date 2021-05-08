package org.vsu.pt.team2.utilitatemmetrisapp.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.vsu.pt.team2.utilitatemmetrisapp.R
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.FragmentAddMeterBinding
import org.vsu.pt.team2.utilitatemmetrisapp.models.MeterType
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.baseFragments.BaseTitledFragment
import org.vsu.pt.team2.utilitatemmetrisapp.ui.setFromVM
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.replaceFragment
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.MeterItemViewModel
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.MeterViewModel

class AddMeterFragment : BaseTitledFragment(R.string.fragment_title_add_meter) {
    private lateinit var binding: FragmentAddMeterBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddMeterBinding.inflate(inflater, container, false)
        initFields(binding)
        return binding.root
    }

    private fun initFields(binding: FragmentAddMeterBinding) {
        binding.meterFound = false
        binding.meterIdentifierExtendededittext.addTextChangedListener(identifierTextChangedListener)
    }

    fun updateMeter(correctAccountIdentifier: String) {
        val meterVM = loadMeter(correctAccountIdentifier)

        binding.meterFound = true
        binding.meterContent.setFromVM(meterVM.toMeterItemVM(), requireContext())
        binding.meterContent.root.setOnClickListener {
            val meterFragment = MeterFragment.createWithVM(meterVM)
            replaceFragment(meterFragment)
        }
    }

    private fun loadMeter(correctAccountIdentifier: String): MeterViewModel {
        //todo подгрузка счётчика
        return MeterViewModel(
            correctAccountIdentifier,
            MeterType.random(),
            452.4,
            123.4,
            432.21,
            102.0,
            false
        )
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
                    updateMeter(it.toString())
                } else
                    binding.meterFound = false
            }
        }

    }
}