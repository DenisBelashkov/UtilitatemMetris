package org.vsu.pt.team2.utilitatemmetrisapp.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.FragmentAddAccountBinding
import org.vsu.pt.team2.utilitatemmetrisapp.models.MeterType
import org.vsu.pt.team2.utilitatemmetrisapp.ui.adapters.MetersListAdapter
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.DisabledDrawerFragment
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.MeterViewModel

class AddAccountFragment : DisabledDrawerFragment() {
    private lateinit var binding: FragmentAddAccountBinding
    private val adapter = MetersListAdapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddAccountBinding.inflate(inflater, container, false)
        initFields(binding)
        return binding.root
    }

    fun initFields(binding: FragmentAddAccountBinding) {
        binding.metersFound = false
        binding.accountIdentifierExtendededittext.addTextChangedListener(
            identifierTextChangedListener
        )
        binding.metersOnAccountRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.metersOnAccountRecyclerView.adapter = adapter
    }

    fun updateMeters(correctAccountIdentifier: String) {
        //todo подгрузка счётчиков
        binding.metersFound = true
        val list = mutableListOf<MeterViewModel>()
        list.add(
            MeterViewModel(
                "7a6d87asd", MeterType.ColdWater, 452.4
            )
        )
        if (correctAccountIdentifier.length % 2 == 0)
            list.add(
                MeterViewModel(
                    "6633pqff445", MeterType.Elect, 1209.1
                )
            )

        adapter.submitList(list)
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
                if (it.length > 6) {
                    updateMeters(it.toString())
                }else
                    binding.metersFound = false
            }
        }

    }
}