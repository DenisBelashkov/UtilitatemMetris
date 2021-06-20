package org.vsu.pt.team2.utilitatemmetrisapp.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.vsu.pt.team2.utilitatemmetrisapp.R
import org.vsu.pt.team2.utilitatemmetrisapp.databinding.FragmentSavedMetersBinding
import org.vsu.pt.team2.utilitatemmetrisapp.managers.MeterManager
import org.vsu.pt.team2.utilitatemmetrisapp.network.ApiResult
import org.vsu.pt.team2.utilitatemmetrisapp.ui.adapters.metersList.MetersWithCheckboxListAdapter
import org.vsu.pt.team2.utilitatemmetrisapp.ui.components.baseFragments.BaseTitledFragment
import org.vsu.pt.team2.utilitatemmetrisapp.ui.tools.*
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.GeneralButtonViewModel
import org.vsu.pt.team2.utilitatemmetrisapp.viewmodels.MeterItemViewModel
import javax.inject.Inject

class SavedMetersFragment : BaseTitledFragment(R.string.fragment_title_saved_meters) {

    private lateinit var binding: FragmentSavedMetersBinding

    @Inject
    lateinit var meterManager: MeterManager

    private val adapter = MetersWithCheckboxListAdapter().apply {
        callbackOnItemLongClick = {
            lifecycleScope.launch {
                replaceFragment(MeterFragment.createWithMeterIdentifier(it.identifier))
            }
        }
        callbackOnCheckedItemsChanged = { checked ->
            val sum = checked.sumOf { it.backlog }
            binding.sumForPay = sum
        }
    }

    fun statusLoading() {
//        binding.fragmentSavedMetersStatusTv.visibility = View.VISIBLE
//        binding.fragmentSavedMetersStatusTv.text = "Загрузка..."
    }

    fun statusLoaded() {
        binding.fragmentSavedMetersStatusTv.visibility = View.GONE
        binding.fragmentSavedMetersStatusTv.text = ""
        binding.fragmentSavedMetersSwipeRefreshLayout.isRefreshing = false
    }

    fun statusLoadedEmptyList() {
        binding.fragmentSavedMetersStatusTv.visibility = View.VISIBLE
        binding.fragmentSavedMetersStatusTv.text = "У вас нету ни одного счётчика"
        binding.fragmentSavedMetersSwipeRefreshLayout.isRefreshing = false
    }

    fun statusNone() {
        binding.fragmentSavedMetersStatusTv.visibility = View.GONE
        binding.fragmentSavedMetersStatusTv.text = ""
        binding.fragmentSavedMetersSwipeRefreshLayout.isRefreshing = false
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
        binding = FragmentSavedMetersBinding.inflate(inflater, container, false)

        binding.savedMetersListRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding.savedMetersListRecyclerView.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.sumForPay = 0.0
        binding.payChosenMetersButton.viewmodel =
            GeneralButtonViewModel(getString(R.string.pay_for_chosen)) {
                adapter.getChecked().let { checked ->
                    if (checked.isNotEmpty()) {
                        val meterIdentifiersForPay: List<String> =
                            checked.map { it.identifier }
                        appCompatActivity()?.replaceFragment(
                            PaymentFragment.createWithMetersIdentifier(meterIdentifiersForPay)
                        )
                    } else {
                        showToast(getString(R.string.select_at_least_1_meter_to_pay))
                    }
                }

            }

        binding.fragmentSavedMetersSwipeRefreshLayout.setOnRefreshListener {
            loadMeters()
        }
        loadMeters()
        super.onViewCreated(view, savedInstanceState)
    }

    fun loadMeters() {
        statusLoading()
        lifecycleScope.launch {
            when (val apiRes = meterManager.getMetersSavedByUser()) {
                is ApiResult.NetworkError -> {
                    statusNone()
                    networkConnectionErrorToast()
                }
                is ApiResult.GenericError -> {
                    statusNone()
                    genericErrorToast(apiRes)
                }
                is ApiResult.Success -> {
                    val list: List<MeterItemViewModel> =
                        apiRes.value.map { MeterItemViewModel(it.identifier, it.type, it.balance, it.address) }
                    adapter.submitList(list)

                    val sum = adapter.getChecked().sumOf { it.backlog }
                    binding.sumForPay = sum

                    if (list.isEmpty())
                        statusLoadedEmptyList()
                    else
                        statusLoaded()
                }
            }
        }
    }
}