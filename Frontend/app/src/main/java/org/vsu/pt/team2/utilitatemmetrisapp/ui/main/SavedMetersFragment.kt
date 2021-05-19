package org.vsu.pt.team2.utilitatemmetrisapp.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.vsu.pt.team2.utilitatemmetrisapp.R
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.FragmentSavedMetersBinding
import org.vsu.pt.team2.utilitatemmetrisapp.managers.MeterManager
import org.vsu.pt.team2.utilitatemmetrisapp.ui.adapters.metersList.MetersWithCheckboxListAdapter
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.baseFragments.BaseTitledFragment
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.appCompatActivity
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.myApplication
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.replaceFragment
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.GeneralButtonViewModel
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.MeterItemViewModel
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.MeterViewModel
import javax.inject.Inject

class SavedMetersFragment : BaseTitledFragment(R.string.fragment_title_saved_meters) {
    @Inject
    lateinit var meterManager: MeterManager

    private val adapter = MetersWithCheckboxListAdapter {
        //todo Получить всю инфу о счётчике из meterRepository например
        val vm = MeterViewModel.fromMeterItemVM(
            it,
            4.86,
            3098.92,
            3124.12,
            false
        )
        replaceFragment(MeterFragment.createWithVM(vm))
    }

    override fun onAttach(context: Context) {
        myApplication()?.appComponent
            ?.meterComponent()
            ?.injectSavedMetersFragment(this)
        super.onAttach(context)
    }

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
                appCompatActivity()?.replaceFragment(PaymentFragment())
            }
        binding.metersListRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding.metersListRecyclerView.adapter = adapter

        updateAdapter()
        return binding.root
    }

    fun updateAdapter() {
//        val list = mutableListOf<MeterItemViewModel>()
//        list.add(
//            MeterItemViewModel(
//                "7a6d87asd", MeterType.ColdWater, 452.4
//            )
//        )
//        list.add(
//            MeterItemViewModel(
//                "6633pqff445", MeterType.Elect, 1209.1
//            )
//        )

        CoroutineScope(Dispatchers.IO).launch {
            val list = meterManager.getMeters()
                .map { MeterItemViewModel(it.identifier, it.type, it.balance) }
            CoroutineScope(Dispatchers.Main).launch {
                adapter.submitList(list)
            }
        }
    }
}