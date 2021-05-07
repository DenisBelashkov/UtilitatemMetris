package org.vsu.pt.team2.utilitatemmetrisapp.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.FragmentAddMeterBinding
import org.vsu.pt.team2.utilitatemmetrisapp.models.MeterType
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.BaseFragment

class AddMeterFragment : BaseFragment() {
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
        //todo подгрузка счётчика
        binding.meterFound = true
        binding.meterContent.apply {
            setBacklogValue(452.4)
            setMeterIdentifier("7a6d87asd")
            meterType = MeterType.ColdWater.name
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
                    updateMeter(it.toString())
                } else
                    binding.meterFound = false
            }
        }

    }
}