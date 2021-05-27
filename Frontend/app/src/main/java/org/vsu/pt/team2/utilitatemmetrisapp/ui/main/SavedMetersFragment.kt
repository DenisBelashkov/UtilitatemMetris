package org.vsu.pt.team2.utilitatemmetrisapp.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.vsu.pt.team2.utilitatemmetrisapp.R
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.FragmentSavedMetersBinding
import org.vsu.pt.team2.utilitatemmetrisapp.managers.MeterManager
import org.vsu.pt.team2.utilitatemmetrisapp.network.ApiResult
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
        lifecycleScope.launch {
            replaceFragment(MeterFragment.createWithMeterIdentifier(it.identifier))
        }
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
        lifecycleScope.launch {
            val apiRes = meterManager.getMetersSavedByUser()
            when (apiRes) {
                is ApiResult.NetworkError -> {
                    //todo show toast
                    view?.let { Snackbar.make(it, "Net connection err", Snackbar.LENGTH_SHORT) }
                }
                is ApiResult.GenericError -> {
                    //todo show toast
                    apiRes.code
                }
                is ApiResult.Success -> {
                    val list =
                        apiRes.value.map { MeterItemViewModel(it.identifier, it.type, it.balance) }
                    adapter.submitList(list)
                }
            }
        }
    }
}