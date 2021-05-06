package org.vsu.pt.team2.utilitatemmetrisapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import org.vsu.pt.team2.utilitatemmetrisapp.R
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.FragmentSavedMetersBinding
import org.vsu.pt.team2.utilitatemmetrisapp.models.MeterType
import org.vsu.pt.team2.utilitatemmetrisapp.ui.adapters.MetersListAdapter
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.BaseFragment
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.appCompatActivity
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.replaceFragment
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.GeneralButtonViewModel
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.MeterViewModel

class SavedMetersFragment : BaseFragment() {

    private val adapter = MetersListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSavedMetersBinding.inflate(inflater, container, false)
        binding.sumForPay = 1
        binding.payChosenMetersButton.viewmodel =
            GeneralButtonViewModel(getString(R.string.pay_for_chosen)) {
                //todo action when pay for all
                appCompatActivity()?.replaceFragment(FragmentPayment())
            }
        binding.metersListRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding.metersListRecyclerView.adapter = adapter

        updateAdapter()
        return binding.root
    }

    fun updateAdapter() {
        val list = mutableListOf<MeterViewModel>()
        list.add(
            MeterViewModel(
                "7a6d87asd", MeterType.ColdWater, 452.4
            )
        )
        list.add(
            MeterViewModel(
                "6633pqff445", MeterType.Elect, 1209.1
            )
        )


        adapter.submitList(list)
    }
}