package org.vsu.pt.team2.utilitatemmetrisapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import org.vsu.pt.team2.utilitatemmetrisapp.R
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.FragmentHistoryBinding
import org.vsu.pt.team2.utilitatemmetrisapp.models.MeterType
import org.vsu.pt.team2.utilitatemmetrisapp.ui.adapters.metersList.MetersHistoryAdapter
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.baseFragments.BaseTitledFragment
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.HistoryMeterItemViewModel

class HistoryFragment : BaseTitledFragment(R.string.fragment_history_title) {
    private lateinit var binding: FragmentHistoryBinding
    private val adapter = MetersHistoryAdapter()

    private fun openPaymentHistoryFragmentForMeterItem(historyMeterItem: HistoryMeterItemViewModel) {
        //todo Найти квитанцию, в которой находится этот счётчик
        //открыть фрагмент с данной квитанцией
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initFields(binding)
        loadData()
    }

    private fun initFields(binding: FragmentHistoryBinding) {

        binding.historyMetersListRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.historyMetersListRecyclerView.adapter = adapter
    }

    private fun loadData() {
        val data = listOf<HistoryMeterItemViewModel>(
            HistoryMeterItemViewModel(
                "765432bhjdskf",
                MeterType.ColdWater,
                123.3,
                "2014:01:12",
                "Ленинский проспект, 102, кв 14"
            ),
            HistoryMeterItemViewModel(
                "345hj345h34",
                MeterType.Elect,
                3423.9,
                "2014:01:12",
                "Ленинский проспект, 102, кв 14"
            ),
            HistoryMeterItemViewModel(
                "j7k567kj56321",
                MeterType.Heating,
                1029.9,
                "2014:01:12",
                "Ленинский проспект, 102, кв 14"
            ),
        )

        adapter.submitList(data)
    }
}